// Adapted from https://github.com/wimdeblauwe/vite-spring-boot
package org.advancedchat.core.vite.thymeleaf

import org.advancedchat.core.vite.configuration.Mode
import org.advancedchat.core.vite.configuration.ViteConfigurationProperties
import org.thymeleaf.context.ITemplateContext
import org.thymeleaf.model.IModel
import org.thymeleaf.model.IOpenElementTag
import org.thymeleaf.processor.element.AbstractElementModelProcessor
import org.thymeleaf.processor.element.IElementModelStructureHandler
import org.thymeleaf.templatemode.TemplateMode

class ViteClientTagProcessor(prefix: String, val properties: ViteConfigurationProperties) :
    AbstractElementModelProcessor(
        TemplateMode.HTML,
        prefix,
        "client",
        true,
        null,
        false,
        10000,
    ) {
  override fun doProcess(
      context: ITemplateContext,
      model: IModel,
      structureHandler: IElementModelStructureHandler,
  ) {
    val event = model.get(0)

    if (event is IOpenElementTag) {
      if (properties.mode == Mode.DEV) {
        val tagFactory = TagFactory(context.modelFactory)

        val (opening, closing) =
            tagFactory.createScriptTag("${properties.devServer.baseUrl}/@vite/client")

        model.replace(0, opening)

        model.replace(model.size() - 1, closing)
      } else {
        model.reset()
      }
    }
  }
}
