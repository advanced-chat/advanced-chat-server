export const serviceCallPattern = /^service:(\w+):call$/
export const serviceResponsePattern = /^service:(\w+):response$/
export const serviceErrorPattern = /^service:(\w+):error$/

export interface EventMessage {
  type: string
  payload: any
}

export interface ServiceCallMessage extends EventMessage {
  id: string
}

export type File = {
  url: string
  name: string
}

export interface TemplateStringParts {
  strings: TemplateStringsArray
  placeholders: string[]
}
