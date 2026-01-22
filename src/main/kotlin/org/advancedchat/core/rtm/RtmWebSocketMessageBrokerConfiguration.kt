package org.advancedchat.core.rtm

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker
class RtmWebSocketMessageBrokerConfiguration : WebSocketMessageBrokerConfigurer {
  override fun configureMessageBroker(config: MessageBrokerRegistry) {
    config.enableSimpleBroker("/queue/", "/topic/")
    config.setApplicationDestinationPrefixes("/application")
  }

  override fun registerStompEndpoints(registry: StompEndpointRegistry) {
    registry.addEndpoint("/rtm")
  }
}
