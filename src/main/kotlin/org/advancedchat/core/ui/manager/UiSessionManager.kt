package org.advancedchat.core.ui.manager

import org.advancedchat.core.ui.model.UiSession

interface UiSessionManager {
  fun createSession(request: CreateUiSessionRequest): UiSession
}
