import type { AxiosInstance } from 'axios'
import axios from 'axios'
import { Subscription, firstValueFrom, take } from 'rxjs'

import { RxStomp, RxStompConfig, RxStompState, StompHeaders, Versions } from '@stomp/rx-stomp'

import type {
  StompX,
  StompXConfiguration,
  StompXConnectOptions,
  StompXError,
  StompXEvent,
  StompXListenForEventOptions,
  StompXPerformActionOptions,
  StompXRelayParameters,
  StompXRelayResourceOptions,
  StompXSubscribeToTopicOptions,
  StompXUploadProgressListener,
  StompXUploadStreamOptions,
} from './definitions'

type PendingAction = {
  types?: string[]
  resolve: (resource: unknown) => void
  reject: (error: StompXError) => void
}

type PendingRelay<R> = {
  resolve: (resource: R) => void
  reject: (error: StompXError) => void
}

export class RxStompX implements StompX {
  private static readonly receiptTimeoutMs = 5000

  private readonly host: string
  private readonly wsScheme: string
  private readonly httpScheme: string
  private readonly rxStompConfig: RxStompConfig
  private readonly axios: AxiosInstance

  private readonly topics: Map<string, Subscription> = new Map()
  private readonly pendingActions: Map<string, PendingAction> = new Map()
  private readonly pendingRelays: Map<string, PendingRelay<unknown>> = new Map()
  private readonly eventHandlers: Map<string, Map<string, Set<(resource: unknown) => void>>> =
    new Map()
  private readonly subscriptions: Set<Subscription> = new Set()

  private rxStomp: RxStomp = new RxStomp()
  private errorsSubscription?: Subscription

  public connected = false

  constructor({ host, secure, debug }: StompXConfiguration) {
    this.host = host

    if (secure === undefined || secure) {
      this.wsScheme = 'wss'
      this.httpScheme = 'https'
    } else {
      this.wsScheme = 'ws'
      this.httpScheme = 'http'
    }

    if (debug) {
      console.log(
        `StompX Configuration: ${JSON.stringify(
          {
            host: this.host,
            secure: secure === undefined || secure,
            wsScheme: this.wsScheme,
            httpScheme: this.httpScheme,
          },
          null,
          2,
        )}`,
      )
    }

    this.ensureCrypto()

    this.rxStompConfig = {
      stompVersions: new Versions(['1.2']),
      connectionTimeout: 60000,
      heartbeatIncoming: 10000,
      heartbeatOutgoing: 60000,
      debug: (message) => {
        if (debug) console.log('StompX Debug:\n' + message)
      },
    }

    this.axios = axios.create({
      baseURL: `${this.httpScheme}://${this.host}`,
    })
  }

  async relayResource<R>({ destination, parameters }: StompXRelayResourceOptions): Promise<R> {
    await this.ensureConnected()

    const subscriptionId = RxStompX.generateSubscriptionId()

    return new Promise<R>((resolve, reject) => {
      this.rxStomp.stompClient.subscribe(
        destination,
        (message) => {
          const pending = this.pendingRelays.get(subscriptionId)
          if (!pending) return

          try {
            const parsed = JSON.parse(message.body)

            pending.resolve(parsed as R)
          } catch (error) {
            pending.reject({
              error: 'StompXRelayParseError',
              message: error instanceof Error ? error.message : 'Failed to parse relay response.',
              timestamp: new Date().toISOString(),
            })
          } finally {
            this.pendingRelays.delete(subscriptionId)
          }
        },
        {
          ...parameters,
          id: subscriptionId,
        },
      )

      this.pendingRelays.set(subscriptionId, {
        resolve: (resource: unknown) => resolve(resource as R),
        reject,
      })
    })
  }
  subscribeToTopic(options: StompXSubscribeToTopicOptions): () => Promise<void> {
    throw new Error('Method not implemented.')
  }
  listenForEvent<E>(options: StompXListenForEventOptions<E>): () => Promise<void> {
    throw new Error('Method not implemented.')
  }
  performAction<R>(options: StompXPerformActionOptions): Promise<R> {
    throw new Error('Method not implemented.')
  }
  uploadStream(options: StompXUploadStreamOptions): Promise<void> {
    throw new Error('Method not implemented.')
  }
  disconnect(): Promise<void> {
    throw new Error('Method not implemented.')
  }

  async connect(options: StompXConnectOptions): Promise<void> {
    const connectHeaders: StompHeaders = {
      'StompX-User': options.username,
    }

    if (options.authParams) {
      connectHeaders['StompX-Auth-Params'] = btoa(JSON.stringify(options.authParams))
    }

    const activate = () => {
      this.rxStomp.configure({
        ...this.rxStompConfig,
        connectHeaders,
      })
      this.rxStomp.activate()
    }

    if (typeof WebSocket === 'function') {
      delete this.rxStompConfig.webSocketFactory
      this.rxStompConfig.brokerURL = `${this.wsScheme}://${this.host}/rtm/websocket`
      activate()
      return
    }

    const SockJSModule = await import('../../vendor/sockjs')
    this.rxStompConfig.webSocketFactory = () => {
      const SockJS = SockJSModule.default
      return new SockJS(`${this.httpScheme}://${this.host}/rtm`)
    }

    console.warn('WebSocket is not available. Falling back to SockJS.')

    activate()

    this.subscriptions.forEach((subscription) => subscription.unsubscribe())
    this.subscriptions.clear()

    const serverHeadersSubscription = this.rxStomp.serverHeaders$.subscribe((headers) => {
      const sessionId = headers['session']
      if (!sessionId) return

      this.rxStomp.configure({
        ...this.rxStompConfig,
        connectHeaders: {
          ...connectHeaders,
          'StompX-Auth-Session-ID': sessionId,
        },
      })
    })

    const stompErrorSubscription = this.rxStomp.stompErrors$.subscribe((frame) => {
      let error: StompXError
      try {
        error = JSON.parse(frame.body)
      } catch {
        error = {
          error: 'UnknownError',
          message: 'An unknown error occurred.',
          timestamp: new Date().toISOString(),
        }
      }

      throw error
    })

    const webSocketErrorSubscription = this.rxStomp.webSocketErrors$.subscribe((err) => {
      console.error(err)

      throw err
    })

    this.subscriptions.add(serverHeadersSubscription)
    this.subscriptions.add(stompErrorSubscription)
    this.subscriptions.add(webSocketErrorSubscription)

    await firstValueFrom(this.rxStomp.connected$.pipe(take(1)))

    if (this.errorsSubscription) {
      this.errorsSubscription.unsubscribe()
      this.errorsSubscription = undefined
    }

    this.errorsSubscription = this.rxStomp
      .watch('/user/queue/errors', {
        id: RxStompX.generateSubscriptionId(),
      })
      .subscribe((message) => {
        const error: StompXError = JSON.parse(message.body)

        const subscription = message.headers['subscription-id']
        const receipt = message.headers['receipt-id']

        if (subscription) {
          const pending = this.pendingRelays.get(subscription)
          if (pending) {
            pending.reject(error)
            pending.subscription?.unsubscribe()
            this.pendingRelays.delete(subscription)
          }
        }

        if (receipt) {
          const pending = this.pendingActions.get(receipt)
          if (pending) {
            pending.reject(error)
            this.pendingActions.delete(receipt)
          }
        }

        if (!subscription && !receipt) {
          this.pendingActions.forEach((pending) => pending.reject(error))
          this.pendingActions.clear()
        }
      })

    this.connected = true
  }

  private async ensureConnected(): Promise<void> {
    await firstValueFrom(this.rxStomp.connected$.pipe(take(1)))
  }

  private ensureCrypto(): void {
    if (globalThis.crypto && typeof globalThis.crypto.getRandomValues === 'function') return

    console.warn('crypto.getRandomValues is not defined. Using insecure Math.random() instead.')
    const fallbackCrypto = {
      getRandomValues: (arr: Uint8Array) => {
        for (let i = 0; i < arr.length; i += 1) {
          arr[i] = Math.floor(Math.random() * 256)
        }
        return arr
      },
    }

    Object.defineProperty(globalThis, 'crypto', {
      value: fallbackCrypto,
      configurable: true,
    })
  }

  private static dataUriToFile(url: string, name: string): File {
    const arr = url.split(',')
    const mime = arr[0]?.match(/:(.*?);/)?.[1]
    const bstr = atob(arr[1] ?? '')
    let n = bstr.length

    const u8arr = new Uint8Array(n)
    while (n--) u8arr[n] = bstr.charCodeAt(n)

    return new File([u8arr], name, { type: mime })
  }

  private static generateSubscriptionId(): string {
    return `subscription-id-${RxStompX.generateId()}`
  }

  private static generateReceipt(): string {
    return `receipt-${RxStompX.generateId()}`
  }

  private static generateId(): string {
    if (typeof globalThis.crypto?.randomUUID === 'function') {
      return globalThis.crypto.randomUUID()
    }

    const bytes = new Uint8Array(16)

    globalThis.crypto.getRandomValues(bytes)

    return Array.from(bytes, (byte) => byte.toString(16).padStart(2, '0')).join('')
  }
}
