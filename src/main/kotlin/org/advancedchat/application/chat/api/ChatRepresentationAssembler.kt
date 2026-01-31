package org.advancedchat.application.chat.api

import io.mcarle.konvert.api.Konverter
import org.advancedchat.application.chat.model.Chat
import org.advancedchat.core.domain.api.ApiRepresentationAssembler
import org.springframework.stereotype.Component

@Component
class ChatRepresentationAssembler(private val converter: ChatConverter = Konverter.get()) :
    ChatConverter by converter, ApiRepresentationAssembler<Chat, ChatRepresentation>
