export {}

declare global {
  interface Window {
    $advancedChatConfiguration: AdvancedChatConfiguration
  }

  interface AdvancedChatConfiguration {
    loadingBar: LoadingBarConfiguration
  }

  interface LoadingBarConfiguration {
    barThickness: number
    barColors: Record<number, string>
    shadowBlur: number
    shadowColor: string
  }
}
