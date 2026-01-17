package org.advancedchat.application

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.modulith.core.ApplicationModules

@SpringBootTest
class AdvancedChatTests {
  private val modules: ApplicationModules = ApplicationModules.of(AdvancedChat::class.java)

  @Test
  fun verifyModules() {
    val verified = modules.verify()

    val violations = verified.detectViolations()

    assertFalse(violations.hasViolations(), "application modules has violations")
  }
}
