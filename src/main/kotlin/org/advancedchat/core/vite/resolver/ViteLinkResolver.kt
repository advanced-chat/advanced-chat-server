package org.advancedchat.core.vite.resolver

import java.util.concurrent.atomic.AtomicReference
import org.advancedchat.core.logging.loggerFactory
import org.advancedchat.core.vite.configuration.Mode
import org.advancedchat.core.vite.configuration.ViteConfigurationProperties
import org.advancedchat.core.vite.model.ManifestEntry
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Component
import tools.jackson.databind.json.JsonMapper

@Component
class ViteLinkResolver(
    private val jsonMapper: JsonMapper,
    private val resourceLoader: ResourceLoader,
    private val properties: ViteConfigurationProperties,
) {
  private val cache = AtomicReference<Map<String, ManifestEntry>>(emptyMap())

  fun resolve(path: String): ManifestEntry? {
    val normalized = normalizeKey(path)

    val manifest = getManifest()
    val entry =
        manifest[normalized]
            ?: manifest[path]
            ?: manifest[stripLeadingSlash(path)]
            ?: manifest[stripLeadingSlash(normalized)]

    if (entry == null) {
      LOGGER.warn("Vite manifest entry not found for path='{}' (normalized='{}')", path, normalized)
    }
    return entry
  }

  private fun getManifest(): Map<String, ManifestEntry> {
    val existing = cache.get()

    if (existing.isNotEmpty()) return existing

    return loadManifest().also { cache.set(it) }
  }

  fun reload() {
    cache.set(loadManifest())
  }

  private fun loadManifest(): Map<String, ManifestEntry> {
    if (properties.mode != Mode.BUILD) return emptyMap()

    val location = "classpath:/static/.vite/manifest.json"

    return try {
      val resource = resourceLoader.getResource(location)

      if (!resource.exists()) {
        LOGGER.warn("Vite manifest not found at {}", location)
        return emptyMap()
      }

      resource.inputStream.use { input ->
        val type =
            jsonMapper.typeFactory.constructMapType(
                Map::class.java,
                String::class.java,
                ManifestEntry::class.java,
            )

        jsonMapper.readValue(input, type)
      }
    } catch (e: Exception) {
      LOGGER.error("Failed to load Vite manifest from $location", e)

      emptyMap()
    }
  }

  private fun normalizeKey(path: String): String = stripLeadingSlash(path).trim()

  private fun stripLeadingSlash(s: String): String = if (s.startsWith("/")) s.drop(1) else s

  companion object {
    private val LOGGER by loggerFactory()
  }
}
