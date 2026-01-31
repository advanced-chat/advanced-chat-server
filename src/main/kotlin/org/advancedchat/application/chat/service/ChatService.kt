package org.advancedchat.application.chat.service

import org.advancedchat.application.chat.model.Chat
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ChatService {
  fun createChat(request: CreateChatRequest): Chat

  fun retrieveChats(pageable: Pageable): Page<Chat>
}
