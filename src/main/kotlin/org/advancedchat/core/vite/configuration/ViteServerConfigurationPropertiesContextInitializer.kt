// Adapted from https://github.com/wimdeblauwe/vite-spring-boot
package org.advancedchat.core.vite.configuration

import java.nio.file.Path
import kotlin.collections.get
import org.advancedchat.core.logging.loggerFactory
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.env.MapPropertySource
import org.springframework.core.env.MutablePropertySources
import tools.jackson.databind.json.JsonMapper

/**
 * This ApplicationContextInitializer will automatically set the property values of {@link
 * ViteDevServerConfigurationProperties} by reading the `server-config.json` file that is generated
 * by the vite-plugin-spring-boot npm package. Due to that, Spring Boot knows where the Vite live
 * reload server is hosting the assets.
 */
class ViteServerConfigurationPropertiesContextInitializer :
    ApplicationContextInitializer<ConfigurableApplicationContext> {
  override fun initialize(context: ConfigurableApplicationContext) {
    val file = DEV_SERVER_CONFIG_PATH.toFile()

    if (file.exists()) {
      val map: MutableMap<*, *> =
          JsonMapper.builder().build().readValue<MutableMap<*, *>?>(file, MutableMap::class.java)!!

      val hostPropertySource =
          MapPropertySource(
              PROPERTY_FILE_PREFIX + "host",
              mapOf(PROPERTY_FILE_PREFIX + "host" to map["host"]!!),
          )

      val portPropertySource =
          MapPropertySource(
              PROPERTY_FILE_PREFIX + "port",
              mapOf(PROPERTY_FILE_PREFIX + "port" to map["port"]!!),
          )

      val propertySources: MutablePropertySources = context.environment.propertySources

      propertySources.addFirst(hostPropertySource)
      propertySources.addFirst(portPropertySource)
    } else {
      LOGGER.debug(
          "Could not find {} - Unable to load information on Vite Dev Server",
          DEV_SERVER_CONFIG_PATH.toAbsolutePath(),
      )
    }
  }

  companion object {
    private val LOGGER by loggerFactory()

    private const val PROPERTY_FILE_PREFIX = "vite.dev-server."

    private val DEV_SERVER_CONFIG_PATH: Path =
        Path.of(
            "build/vite-spring-boot-plugin",
            "dev-server-config.json",
        )
  }
}
