package org.advancedchat.application.chat.api

import org.advancedchat.application.chat.model.Chat
import org.springframework.hateoas.EntityModel

data class ChatRepresentation(val id: Long, val name: String) : EntityModel<Chat>()
