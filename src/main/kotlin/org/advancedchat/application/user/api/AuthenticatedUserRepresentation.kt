package org.advancedchat.application.user.api

import org.springframework.hateoas.RepresentationModel

data class AuthenticatedUserRepresentation(val name: String) :
    RepresentationModel<AuthenticatedUserRepresentation>()
