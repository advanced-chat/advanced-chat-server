/*
 * © 2021 - Present ChatKitty, Inc. All rights reserved.
 *
 * This file contains proprietary and confidential information.
 * Unauthorized copying, distribution, or disclosure of this information,
 * is strictly prohibited, except as expressly authorized in writing.
 */
package org.advancedchat.core.rtm.interceptor

import org.springframework.boot.info.BuildProperties
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.messaging.simp.SimpMessageType
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Component

@Component
class ConnectedChannelInterceptor(private val buildProperties: BuildProperties) :
    ChannelInterceptor {
  private val server by lazy {
    val version = buildProperties.version

    "AdvancedChat/$version"
  }

  override fun preSend(message: Message<*>, channel: MessageChannel): Message<*>? {
    val headers = message.headers

    val messageType = SimpMessageHeaderAccessor.getMessageType(headers)

    if (messageType == SimpMessageType.CONNECT_ACK) {
      val heartbeat = SimpMessageHeaderAccessor.getHeartbeat(headers)

      val accessor =
          StompHeaderAccessor.create(StompCommand.CONNECTED).apply {
            sessionId = SimpMessageHeaderAccessor.getSessionId(headers)

            setNativeHeader("version", "1.2")
            setNativeHeader("server", server)
            setNativeHeader("session", sessionId)
            setNativeHeader("replies", "/user/queue/replies")
            setNativeHeader("notifications", "/user/queue/notifications")
            setNativeHeader("errors", "/user/queue/errors")

            heartbeat?.let { setHeartbeat(it[0], it[1]) }
          }

      return MessageBuilder.createMessage(message.payload, accessor.messageHeaders)
    }

    return super.preSend(message, channel)
  }
}
