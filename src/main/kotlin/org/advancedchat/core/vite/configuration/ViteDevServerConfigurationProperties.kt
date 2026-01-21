package org.advancedchat.core.vite.configuration

data class ViteDevServerConfigurationProperties(val host: String, val port: Int) {
  val baseUrl: String
    get() = "//$host:$port"
}
