package org.advancedchat.core.logging

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.full.companionObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun <R : Any> R.loggerFactory(): ReadOnlyProperty<R, Logger> {
  val kClass = this::class

  val javaClass = kClass.java

  val clazz = if (kClass.companionObject != null) javaClass.enclosingClass else javaClass

  return ReadOnlyProperty { _, _ -> LoggerFactory.getLogger(clazz) }
}
