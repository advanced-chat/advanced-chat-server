package org.advancedchat.application.ui.model

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.event

class SseUiSession(override val id: String, override val username: String) : UiSession {
  private val emitter = SseEmitter(0L)

  override fun initialize(): SseEmitter {
    emitter.send("ac:setup-application") { data(mapOf("username" to username)) }

    return emitter
  }

  private fun SseEmitter.send(event: String, action: SseEmitter.SseEventBuilder.() -> Unit) =
      send(
          event().name(event).apply(action).build(),
      )
}
