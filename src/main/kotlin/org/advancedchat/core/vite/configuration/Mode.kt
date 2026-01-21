// Adapted from https://github.com/wimdeblauwe/vite-spring-boot
package org.advancedchat.core.vite.configuration

enum class Mode {
  /** Vite is running in live reload mode. */
  DEV,

  /** Vite is running in build (production) mode. */
  BUILD,
}
