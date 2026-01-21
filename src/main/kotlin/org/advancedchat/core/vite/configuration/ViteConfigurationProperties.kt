// Adapted from https://github.com/wimdeblauwe/vite-spring-boot
package org.advancedchat.core.vite.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.DefaultValue

@ConfigurationProperties("vite")
data class ViteConfigurationProperties(
    @DefaultValue("build") val mode: Mode,
    val devServer: ViteDevServerConfigurationProperties =
        ViteDevServerConfigurationProperties(
            "localhost",
            5173,
        ),
)
