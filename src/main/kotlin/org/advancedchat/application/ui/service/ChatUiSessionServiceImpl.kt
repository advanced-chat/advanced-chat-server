package org.advancedchat.application.ui.service

import java.util.concurrent.ConcurrentHashMap
import org.advancedchat.application.ui.model.SseUiSession
import org.advancedchat.application.ui.model.UiSession
import org.springframework.stereotype.Component

@Component
class ChatUiSessionServiceImpl : UiSessionService {
  private val sessions: MutableMap<String, SseUiSession> = ConcurrentHashMap()

  override fun createSession(request: CreateUiSessionRequest): SseUiSession =
      with(request) {
        val session = SseUiSession(id, username)

        sessions[session.id] = session

        return session
      }

  override fun getSession(id: String): UiSession =
      sessions[id] ?: throw IllegalStateException("Session with id $id not found")
}
