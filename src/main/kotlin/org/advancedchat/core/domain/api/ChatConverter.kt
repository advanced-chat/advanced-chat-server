package org.advancedchat.core.domain.api

import org.advancedchat.core.domain.model.DomainModel
import org.springframework.core.convert.converter.Converter
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.RepresentationModelAssembler

interface ApiRepresentationAssembler<M : DomainModel, R : EntityModel<M>> :
    Converter<M, R>, RepresentationModelAssembler<M, R> {
  override fun convert(source: M): R

  override fun toModel(entity: M): R = convert(entity)
}
