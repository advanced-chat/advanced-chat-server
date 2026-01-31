package org.advancedchat.application.chat.service

import org.advancedchat.application.chat.model.Chat
import org.advancedchat.application.chat.repository.ChatRepository
import org.advancedchat.core.domain.service.DomainService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

@DomainService
class ChatServiceImpl(private val repository: ChatRepository) : ChatService {
  override fun createChat(request: CreateChatRequest): Chat = repository.save(Chat(request.name))

  override fun retrieveChats(pageable: Pageable): Page<Chat> = repository.findAll(pageable)
}
