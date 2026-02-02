export type SetupApplicationOptions = {
  username: string
}

export interface ApplicationCoordinator {
  setupApplication(options: SetupApplicationOptions): Promise<void>
  tearDownApplication(): Promise<void>
}
