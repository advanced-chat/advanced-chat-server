package org.advancedchat.core.boot

import org.advancedchat.application.chat.service.ChatService
import org.advancedchat.application.chat.service.CreateChatRequest
import org.advancedchat.application.user.service.CreateUserRequest
import org.advancedchat.application.user.service.UserService
import org.advancedchat.core.uuid.uuid
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("dev")
class DemoApplicationRunner(
    private val chatService: ChatService,
    private val userService: UserService,
) : ApplicationRunner {
  override fun run(args: ApplicationArguments) {
    if (userService.retrieveUser("test") == null) {
      userService.createUser(CreateUserRequest("test", "Test User"))
    }

    chatService.createChat(CreateChatRequest(uuid()))
  }
}
