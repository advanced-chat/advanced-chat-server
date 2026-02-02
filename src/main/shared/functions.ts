import type { File, TemplateStringParts } from './definitions'

type NestedObject = { [key: string | number]: any }
type NestedArray = any[]
type NestedStructure = NestedObject | NestedArray

export const getIn = (
  structure: NestedStructure | null | undefined,
  keys: (string | number)[],
  defaultValue?: any,
): any => {
  if (!keys.length) return structure || defaultValue
  if (!structure) return defaultValue

  const [firstKey, ...remainingKeys] = keys

  if (typeof firstKey === 'number' && isNestedArray(structure)) {
    return getIn(structure[firstKey], remainingKeys, defaultValue)
  } else if (firstKey && isNestedObject(structure)) {
    return getIn((structure as NestedObject)[firstKey], remainingKeys, defaultValue)
  }

  return defaultValue
}

const isNestedArray = (structure: any): structure is NestedArray => Array.isArray(structure)

const isNestedObject = (structure: any): structure is NestedObject =>
  typeof structure === 'object' && structure !== null

export const serializable = (value: any) => {
  if (typeof value === 'undefined') return undefined

  if (typeof value === 'function') return undefined

  return JSON.parse(JSON.stringify(value))
}

export const uuid = (): string => {
  const randomValues = new Uint8Array(16)
  crypto.getRandomValues(randomValues)

  // Adjust specific bits for UUID v4 compliance
  randomValues[6] = (randomValues[6] & 0x0f) | 0x40
  randomValues[8] = (randomValues[8] & 0x3f) | 0x80

  // Convert to hex
  const toHex = (byte: number) => byte.toString(16).padStart(2, '0')

  // Form the UUID string
  return `${toHex(randomValues[0])}${toHex(randomValues[1])}${toHex(randomValues[2])}${toHex(
    randomValues[3],
  )}-${toHex(randomValues[4])}${toHex(randomValues[5])}-${toHex(randomValues[6])}${toHex(
    randomValues[7],
  )}-${toHex(randomValues[8])}${toHex(randomValues[9])}-${toHex(randomValues[10])}${toHex(
    randomValues[11],
  )}${toHex(randomValues[12])}${toHex(randomValues[13])}${toHex(randomValues[14])}${toHex(
    randomValues[15],
  )}`
}

const zeroPad = (num: number, pad: number) => {
  return String(num).padStart(pad, '0')
}

const isSameDay = (d1: Date, d2: Date) => {
  return (
    d1.getFullYear() === d2.getFullYear() &&
    d1.getMonth() === d2.getMonth() &&
    d1.getDate() === d2.getDate()
  )
}

export const parseTimestamp = (timestamp: string, format = '', locale = 'en-GB') => {
  if (!timestamp) return

  const date = new Date(timestamp)

  if (format === 'HH:mm') {
    return `${zeroPad(date.getHours(), 2)}:${zeroPad(date.getMinutes(), 2)}`
  } else if (format === 'DD MMMM YYYY') {
    const options: Intl.DateTimeFormatOptions = { month: 'long', year: 'numeric', day: 'numeric' }
    return `${new Intl.DateTimeFormat(locale, options).format(date)}`
  } else if (format === 'DD/MM/YY') {
    const options: Intl.DateTimeFormatOptions = {
      month: 'numeric',
      year: 'numeric',
      day: 'numeric',
    }
    return `${new Intl.DateTimeFormat(locale, options).format(date)}`
  } else if (format === 'DD MMMM, HH:mm') {
    const options: Intl.DateTimeFormatOptions = { month: 'long', day: 'numeric' }
    return `${new Intl.DateTimeFormat(locale, options).format(date)}, ${zeroPad(
      date.getHours(),
      2,
    )}:${zeroPad(date.getMinutes(), 2)}`
  }

  return date
}

export const formatTimestamp = (date: Date, timestamp: string, locale: string) => {
  const timestampFormat = isSameDay(date, new Date()) ? 'HH:mm' : 'DD/MM/YY'
  const result = parseTimestamp(timestamp, timestampFormat, locale)
  return timestampFormat === 'HH:mm' ? `Today, ${result}` : result
}

export const clean = (structure: any) => {
  const cleanItem = (item: any): any => {
    if (item && typeof item === 'object') {
      return Object.entries(item).reduce((acc, [key, value]) => {
        const cleaned = cleanItem(value)

        if (cleaned && Object.keys(cleaned).length > 0) {
          acc[key] = cleaned
        }

        return acc
      }, {} as any)
    } else {
      return item
    }
  }

  return cleanItem(structure)
}

export const download = async ({ url, name }: File) => {
  try {
    //window.progressbar.show()

    const response = await fetch(url)

    if (!response.body) return

    const reader = response.body.getReader()

    const contentLength = +(response.headers.get('Content-Length') || 0)

    const chunks = []

    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    let receivedLength = 0

    const stream = new ReadableStream({
      async start(controller) {
        while (reader) {
          const { done, value } = await reader.read()

          if (done) break

          chunks.push(value)

          receivedLength += value.length

          //window.progressbar.progress(receivedLength / contentLength)

          controller.enqueue(value)
        }

        controller.close()
      },
    })

    const responseStream = new Response(stream)

    const blob = await responseStream.blob()

    const blobUrl = URL.createObjectURL(blob)

    const link = document.createElement('a')

    link.href = blobUrl
    link.download = name

    document.body.appendChild(link)

    //window.progressbar.hide()

    link.click()

    document.body.removeChild(link)

    URL.revokeObjectURL(blobUrl)
  } catch (error) {
    //window.progressbar.hide()

    console.error('Error downloading file:', error)
  }
}

export const parseTemplateString = (raw: string): TemplateStringParts => {
  const regex = /\$\((.*?)\)/g

  const literals: string[] = []
  const placeholders: string[] = []

  let match: RegExpExecArray | null

  let lastIndex = 0
  while ((match = regex.exec(raw)) !== null) {
    literals.push(raw.slice(lastIndex, match.index))

    placeholders.push(match[1])

    lastIndex = regex.lastIndex
  }

  if (lastIndex < raw.length) {
    literals.push(raw.slice(lastIndex))
  } else {
    literals.push('')
  }

  const strings: any = Object.assign([], literals)

  Object.defineProperty(strings, 'raw', {
    value: literals,
  })

  return { strings, placeholders }
}

export const delay = (milliseconds: number) =>
  new Promise((resolve: (_: void) => void) => setTimeout(resolve, milliseconds))
