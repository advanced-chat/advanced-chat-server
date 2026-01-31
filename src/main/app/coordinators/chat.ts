import type { ApplicationCoordinator, SetupApplicationOptions } from './types.ts'
import { type App, createApp } from 'vue'

import GenericChatTemplate from '../templates/GenericChatTemplate.vue'

export type SetupGenericChatApplicationOptions =
  | SetupApplicationOptions
  | {
      chatId: string
    }

export class GenericChatApplicationCoordinator implements ApplicationCoordinator {
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

  setupApplication(options: SetupGenericChatApplicationOptions): Promise<void> {
    console.log('Setting up chat application', options)

    const app = createApp(GenericChatTemplate)

    app.mount('#app')

    this.app = app

    return Promise.resolve(undefined)
  }

  tearDownApplication(): Promise<void> {
    console.log('Tearing down chat application')

    this.app?.unmount()

    return Promise.resolve(undefined)
  }
}
