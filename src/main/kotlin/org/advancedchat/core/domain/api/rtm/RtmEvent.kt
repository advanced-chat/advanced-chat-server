package org.advancedchat.core.domain.api.rtm

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.hateoas.RepresentationModel

@JsonInclude(JsonInclude.Include.NON_NULL)
data class RtmEvent<T>(val type: String, val resource: T?) : RepresentationModel<RtmEvent<T>>()
