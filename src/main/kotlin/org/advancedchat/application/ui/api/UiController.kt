package org.advancedchat.application.ui.api

import jakarta.servlet.http.HttpSession
import org.advancedchat.application.ui.service.CreateUiSessionRequest
import org.advancedchat.application.ui.service.UiSessionService
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@Controller
@RequestMapping("/")
class UiController(private val uiSessionService: UiSessionService) {
  @GetMapping
  fun index(): String {
    return "index"
  }

  @GetMapping("/ui", produces = [MediaType.APPLICATION_JSON_VALUE])
  @ResponseBody
  fun initializeUi(session: HttpSession): UiSessionRepresentation {
    val uiSession =
        uiSessionService.createSession(
            CreateUiSessionRequest(
                session.id,
                "test-user",
            ),
        )

    return UiSessionRepresentation(session.id, uiSession.username, "generic-chat")
  }

  @GetMapping("/ui/stream", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
  fun streamUi(session: HttpSession): SseEmitter {
    val session =
        uiSessionService.createSession(
            CreateUiSessionRequest(
                session.id,
                "test-user",
            ),
        )

    return session.initialize()
  }
}
