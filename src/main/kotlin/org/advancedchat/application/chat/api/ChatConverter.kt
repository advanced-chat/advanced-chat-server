package org.advancedchat.application.chat.api

import io.mcarle.konvert.api.Konverter
import org.advancedchat.application.chat.model.Chat

@Konverter
interface ChatConverter {
  fun convert(source: Chat): ChatRepresentation
}
