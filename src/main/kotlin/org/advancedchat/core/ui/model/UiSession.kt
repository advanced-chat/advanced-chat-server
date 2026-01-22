package org.advancedchat.core.ui.model

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.event

class UiSession(val emitter: SseEmitter = SseEmitter(0L)) {
  fun send(event: String, action: SseEmitter.SseEventBuilder.() -> Unit) =
      emitter.send(
          event().name(event).apply(action).build(),
      )
}
