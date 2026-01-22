package org.advancedchat.core.mediatype.hal

import com.fasterxml.jackson.annotation.JsonInclude
import org.advancedchat.core.mediatype.AdvancedChatMediaTypes.json
import org.springframework.hateoas.config.HypermediaMappingInformation
import org.springframework.hateoas.mediatype.hal.HalMediaTypeConfiguration
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import tools.jackson.databind.PropertyNamingStrategies
import tools.jackson.databind.json.JsonMapper

@Component
class AdvancedChatResourceMappingInformation(private val delegate: HalMediaTypeConfiguration) :
    HypermediaMappingInformation {
  override fun getMediaTypes(): List<MediaType> = listOf(json, MediaType.APPLICATION_JSON)

  override fun configureJsonMapper(builder: JsonMapper.Builder): JsonMapper.Builder =
      delegate
          .configureJsonMapper(builder)
          .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
          .changeDefaultPropertyInclusion { it.withValueInclusion(JsonInclude.Include.NON_NULL) }
}
