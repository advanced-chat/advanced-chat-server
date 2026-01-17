package org.advancedchat.application.user

import org.advancedchat.application.user.service.UserService
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserTests(private val service: UserService) {
  @Test
  fun verifyCreateUser() {
    assertFalse(service.readUser("test") != null)
  }
}
