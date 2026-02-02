import { inject, ref } from 'vue'
import { defineStore } from 'pinia'
import { STOMPX_SYMBOL } from '../providers/symbols.ts'

export interface User {
  name: string
  displayName: string
}

export const useUserStore = defineStore('user', () => {
  const stompX = inject(STOMPX_SYMBOL)!

  const user = ref<User | null>(null)

  const setup = async () => {
    user.value = await stompX.relayResource<User>({
      destination: '/application/user.relay',
    })
  }

  return { user, setup }
})
