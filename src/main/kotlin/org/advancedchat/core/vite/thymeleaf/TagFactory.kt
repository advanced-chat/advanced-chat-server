// Adapted from https://github.com/wimdeblauwe/vite-spring-boot
package org.advancedchat.core.vite.thymeleaf

import org.thymeleaf.model.*

class TagFactory(private val modelFactory: IModelFactory) {
  fun createScriptTag(src: String): Pair<IElementTag, IElementTag> {
    val openTag =
        modelFactory.createOpenElementTag(
            "script",
            mapOf("type" to "module", "src" to src),
            AttributeValueQuotes.DOUBLE,
            false,
        )

    val closeTag = modelFactory.createCloseElementTag("script")

    return openTag to closeTag
  }

  fun generateCssTag(href: String): IStandaloneElementTag =
      modelFactory.createStandaloneElementTag(
          "link",
          mapOf("rel" to "stylesheet", "href" to href),
          null,
          false,
          true,
      )
}
