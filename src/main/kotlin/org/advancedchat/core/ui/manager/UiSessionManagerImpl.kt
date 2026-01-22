package org.advancedchat.core.ui.manager

import org.advancedchat.core.ui.model.UiSession
import org.springframework.stereotype.Component

@Component
class UiSessionManagerImpl : UiSessionManager {
  override fun createSession(request: CreateUiSessionRequest): UiSession {
    val session = UiSession()

    val principal = request.principal

    session.send("ac:configure-chat-service") { data(mapOf("username" to principal.name)) }

    return session
  }
}
