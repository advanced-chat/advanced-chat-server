package org.advancedchat.application.ui.service

import org.advancedchat.application.ui.model.UiSession

interface UiSessionService {
  fun createSession(request: CreateUiSessionRequest): UiSession

  fun getSession(id: String): UiSession
}
