package org.advancedchat.core.controller

import org.advancedchat.core.ui.manager.CreateUiSessionRequest
import org.advancedchat.core.ui.manager.UiSessionManager
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@Controller
@RequestMapping("/")
class MainController(private val uiSessionManager: UiSessionManager) {
  @GetMapping
  fun index(): String {
    return "index"
  }

  @GetMapping("/ui", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
  fun connect(): SseEmitter {
    return uiSessionManager.createSession(CreateUiSessionRequest { "test-user" }).emitter
  }
}
