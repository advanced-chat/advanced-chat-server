// Adapted from https://github.com/wimdeblauwe/vite-spring-boot
package org.advancedchat.core.vite.thymeleaf

import org.advancedchat.core.vite.configuration.ViteConfigurationProperties
import org.advancedchat.core.vite.resolver.ViteLinkResolver
import org.springframework.stereotype.Component
import org.thymeleaf.dialect.AbstractProcessorDialect
import org.thymeleaf.processor.IProcessor

@Component
class ViteThymeleafDialect(
    private val properties: ViteConfigurationProperties,
    private val linkResolver: ViteLinkResolver,
) : AbstractProcessorDialect("Vite Dialect", "vite", 1000) {
  override fun getProcessors(prefix: String): Set<IProcessor> =
      setOf(
          ViteClientTagProcessor(prefix, properties),
          ViteTagProcessor(prefix, linkResolver, properties),
      )
}
