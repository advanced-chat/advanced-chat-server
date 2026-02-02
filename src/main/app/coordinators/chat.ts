import type { ApplicationCoordinator, SetupApplicationOptions } from './definitions.ts'
import { type App, createApp } from 'vue'

import GenericChatTemplate from '../templates/GenericChatTemplate.vue'
import { createPinia } from 'pinia'
import { STOMPX_SYMBOL } from '../providers/symbols.ts'
import { RxStompX } from '../../api/stompx'
import { useUserStore } from '../stores/user.ts'

export type SetupGenericChatApplicationOptions = SetupApplicationOptions

export class GenericChatApplicationCoordinator implements ApplicationCoordinator {
  private readonly pinia = createPinia()

  private app?: App

  constructor(source: EventSource) {
    console.log('Creating chat application coordinator')

    source.addEventListener('ac:setup-application', async (event) => {
      await this.setupApplication(JSON.parse(event.data))
    })

    source.addEventListener('ac:tear-down-application', async () => {
      await this.tearDownApplication()
    })
  }

  async setupApplication(options: SetupGenericChatApplicationOptions) {
    console.log('Setting up chat application', options)

    const app = createApp(GenericChatTemplate)

    const stompX = new RxStompX({
      host: 'localhost:8080',
      secure: false,
    })

    await stompX.connect({
      username: options.username,
    })

    app.provide(STOMPX_SYMBOL, stompX)

    app.use(this.pinia)

    const { setup: setupUser } = useUserStore()

    await setupUser()

    app.mount('#app')

    this.app = app
  }

  tearDownApplication(): Promise<void> {
    console.log('Tearing down chat application')

    this.app?.unmount()

    return Promise.resolve(undefined)
  }
}
