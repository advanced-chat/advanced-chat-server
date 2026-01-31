import type { ApplicationCoordinator } from './types.ts'

export type ApplicationCoordinatorOptions = {
  bundle: 'generic-chat'
}

export class ApplicationCoordinatorFactory {
  async create(options: ApplicationCoordinatorOptions): Promise<ApplicationCoordinator> {
    switch (options.bundle) {
      case 'generic-chat': {
        const { GenericChatApplicationCoordinator } = await import('./chat.ts')

        return new GenericChatApplicationCoordinator(new EventSource('/ui/stream'))
      }
    }
  }
}
