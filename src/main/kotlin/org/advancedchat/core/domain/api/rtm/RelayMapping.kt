package org.advancedchat.core.domain.api.rtm

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
)
@MustBeDocumented
annotation class RelayMapping(val type: String)
