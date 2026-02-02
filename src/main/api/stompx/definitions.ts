export type StompXRelayParameters = Record<string, any>

export declare class StompXRelayResourceOptions {
  destination: string
  parameters?: StompXRelayParameters
}

export declare class StompXPage {
  _embedded?: Record<string, unknown>
  page: StompXPageMetadata
  _relays: StompXPageRelays
}

export declare class StompXPageMetadata {
  size: number
  totalElement: number
  totalPages: number
  number: number
  start: number
  next: number
}

export declare class StompXPageRelays {
  first?: string
  prev?: string
  self: string
  next?: string
  last?: string
}

export declare class StompXError {
  error: string
  message: string
  timestamp: string
  coolOffDuration?: string
}

export declare class StompXConfiguration {
  host: string
  secure?: boolean
  debug?: boolean
}

export declare class StompXConnectOptions {
  username: string
  authParams?: unknown
}

export declare class StompXListenForEventOptions<E> {
  topic: string
  event: string
  onEvent: (event: E) => void
}

export declare class StompXSubscribeToTopicOptions {
  topic: string
}

export declare class StompXPerformActionOptions {
  destination: string
  body: unknown
  events?: string[]
}

export declare class StompXUploadStreamOptions {
  stream: string
  grant: string
  file: File | { uri: string; name: string }
  properties?: Map<string, unknown>
  progressListener?: StompXUploadProgressListener
}

export declare class StompXEvent<R> {
  type: string
  version: string
  resource: R
}

export declare class StompXEventHandler<R> {
  event: string
  onEvent: (resource: R) => void
}

export interface StompXUploadProgressListener {
  onStarted?: () => void
  onProgress?: (progress: number) => void
  onCompleted?: () => void
  onFailed?: () => void
  onCancelled?: () => void
}

export interface StompX {
  connected: boolean
  connect(options: StompXConnectOptions): Promise<void>
  relayResource<R>(options: StompXRelayResourceOptions): Promise<R>
  subscribeToTopic(options: StompXSubscribeToTopicOptions): () => Promise<void>
  listenForEvent<E>(options: StompXListenForEventOptions<E>): () => Promise<void>
  performAction<R>(options: StompXPerformActionOptions): Promise<R>
  uploadStream(options: StompXUploadStreamOptions): Promise<void>
  disconnect(): Promise<void>
}
