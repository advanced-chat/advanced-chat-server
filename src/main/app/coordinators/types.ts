export type SetupApplicationOptions = {
  name: string
}

export interface ApplicationCoordinator {
  setupApplication(options: SetupApplicationOptions): Promise<void>
  tearDownApplication(): Promise<void>
}
