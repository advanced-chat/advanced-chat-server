package org.advancedchat.boot

import org.advancedchat.application.user.service.CreateUserRequest
import org.advancedchat.application.user.service.UserService
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("demo")
class DemoApplicationRunner(private val service: UserService) : ApplicationRunner {
  override fun run(args: ApplicationArguments) {
    if (service.readUser("test") != null) return

    service.createUser(CreateUserRequest("test", "Test User"))
  }
}
