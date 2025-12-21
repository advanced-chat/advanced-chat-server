package org.advancedchat.server.configuration

import com.opentable.db.postgres.embedded.EmbeddedPostgres
import javax.sql.DataSource
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class TestPostgresConfiguration {
  companion object {
    private val embeddedPostgres: EmbeddedPostgres = EmbeddedPostgres.start()

    class EmbeddedPostgresExtension : AfterAllCallback {
      override fun afterAll(context: ExtensionContext) {
        embeddedPostgres.close()
      }
    }
  }

  @Bean fun datasource(): DataSource = embeddedPostgres.postgresDatabase
}
