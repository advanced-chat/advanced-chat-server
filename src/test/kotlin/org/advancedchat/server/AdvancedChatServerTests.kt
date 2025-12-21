package org.advancedchat.server

import org.advancedchat.server.configuration.TestPostgresConfiguration
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.modulith.core.ApplicationModules

@SpringBootTest
@Import(TestPostgresConfiguration::class)
@ExtendWith(TestPostgresConfiguration.Companion.EmbeddedPostgresExtension::class)
class AdvancedChatServerTests {
  private val modules: ApplicationModules = ApplicationModules.of(AdvancedChatServer::class.java)

  @Test
  fun verifyModules() {
    val verified = modules.verify()

    val violations = verified.detectViolations()

    assertFalse(violations.hasViolations(), "application modules has violations")
  }
}
