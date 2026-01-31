package org.advancedchat.application.ui.model

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

interface UiSession {
  val id: String
  val username: String

  fun initialize(): SseEmitter
}
