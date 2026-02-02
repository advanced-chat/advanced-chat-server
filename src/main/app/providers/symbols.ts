import { type InjectionKey } from 'vue'
import type { StompX } from '../../api/stompx/definitions.ts'

export const STOMPX_SYMBOL: InjectionKey<StompX> = Symbol.for('advanced-chat:stompx')
