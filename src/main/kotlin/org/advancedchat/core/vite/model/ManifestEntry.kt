// Adapted from https://github.com/wimdeblauwe/vite-spring-boot
package org.advancedchat.core.vite.model

data class ManifestEntry(
    val file: String,
    val src: String,
    val isEntry: Boolean,
    val css: List<String>?,
    val imports: List<String>?,
)
