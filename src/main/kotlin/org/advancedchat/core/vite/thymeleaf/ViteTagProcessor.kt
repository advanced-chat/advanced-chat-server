package org.advancedchat.core.vite.thymeleaf

import org.advancedchat.core.logging.loggerFactory
import org.advancedchat.core.vite.configuration.Mode
import org.advancedchat.core.vite.configuration.ViteConfigurationProperties
import org.advancedchat.core.vite.resolver.ViteLinkResolver
import org.thymeleaf.context.ITemplateContext
import org.thymeleaf.model.*
import org.thymeleaf.processor.element.AbstractElementModelProcessor
import org.thymeleaf.processor.element.IElementModelStructureHandler
import org.thymeleaf.templatemode.TemplateMode

class ViteTagProcessor(
    dialectPrefix: String,
    private val linkResolver: ViteLinkResolver,
    private val properties: ViteConfigurationProperties,
) :
    AbstractElementModelProcessor(
        TemplateMode.HTML,
        dialectPrefix,
        TAG_NAME,
        true,
        null,
        false,
        PRECEDENCE,
    ) {

  override fun doProcess(
      context: ITemplateContext,
      model: IModel,
      structureHandler: IElementModelStructureHandler,
  ) {
    val modelFactory = context.modelFactory
    val visitor = ViteModelVisitor(modelFactory)

    model.accept(visitor)
    model.reset()

    visitor.htmlEntries.forEach { model.add(it) }
  }

  private fun isCssPath(value: String): Boolean =
      value.endsWith(".css") ||
          value.endsWith(".scss") ||
          value.endsWith(".sass") ||
          value.endsWith(".less") ||
          value.endsWith(".styl") ||
          value.endsWith(".stylus") ||
          value.endsWith(".pcss") ||
          value.endsWith(".postcss")

  private fun ensureLeadingSlash(path: String): String =
      if (path.startsWith("/")) path else "/$path"

  private inner class ViteModelVisitor(modelFactory: IModelFactory) : AbstractModelVisitor() {
    val htmlEntries = mutableListOf<ITemplateEvent>()

    private val tagFactory = TagFactory(modelFactory)
    private val references = mutableSetOf<String>()

    override fun visit(openElementTag: IOpenElementTag) {
      val elementName = openElementTag.elementDefinition.elementName.elementName

      if (elementName == ENTRY_TAG_NAME) {
        val valueAttribute = openElementTag.getAttributeValue("value")
        if (!valueAttribute.isNullOrBlank()) {
          handleValue(valueAttribute.trim())
        }
      }
    }

    private fun handleValue(value: String) {
      LOGGER.info("Resolving {}", value)

      if (properties.mode == Mode.DEV) {
        val src = "${properties.devServer.baseUrl}/${value.removePrefix("/")}"

        executeIfNotOutputtedYet(src) {
          val (opening, closing) = tagFactory.createScriptTag(src)

          htmlEntries.add(opening)
          htmlEntries.add(closing)
        }

        return
      }

      handleImportedResource(value)
    }

    private fun handleImportedResource(resource: String) {
      LOGGER.debug("Handling imported resource: {}", resource)

      val manifestEntry = linkResolver.resolve(resource) ?: return

      val filePath = ensureLeadingSlash(manifestEntry.file)

      if (isCssPath(filePath)) {
        executeIfNotOutputtedYet(filePath) { htmlEntries.add(tagFactory.generateCssTag(filePath)) }
      } else {
        executeIfNotOutputtedYet(filePath) {
          val (opening, closing) = tagFactory.createScriptTag(filePath)

          htmlEntries.add(opening)
          htmlEntries.add(closing)
        }
      }

      manifestEntry.css?.forEach { linkedCss ->
        val cssPath = ensureLeadingSlash(linkedCss)
        executeIfNotOutputtedYet(cssPath) { htmlEntries.add(tagFactory.generateCssTag(cssPath)) }
      }

      manifestEntry.imports?.forEach { nestedImportedResource ->
        handleImportedResource(nestedImportedResource)
      }
    }

    private fun executeIfNotOutputtedYet(value: String, action: () -> Unit) {
      if (references.add(value)) {
        action()
      }
    }
  }

  companion object {
    private val LOGGER by loggerFactory()
    private const val TAG_NAME = "vite" // <vite:vite>...</vite:vite>
    private const val ENTRY_TAG_NAME = "entry" // <vite:entry value="..."/>
    private const val PRECEDENCE = 1000
  }
}
