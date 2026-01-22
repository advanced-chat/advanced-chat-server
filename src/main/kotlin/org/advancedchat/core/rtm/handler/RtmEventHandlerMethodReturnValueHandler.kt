package org.advancedchat.core.rtm.handler

import kotlin.jvm.java
import org.advancedchat.core.domain.api.rtm.RelayMapping
import org.advancedchat.core.domain.api.rtm.RtmEvent
import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.getBean
import org.springframework.core.MethodParameter
import org.springframework.messaging.Message
import org.springframework.messaging.handler.invocation.HandlerMethodReturnValueHandler
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.messaging.simp.SimpMessageType
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.messaging.simp.user.DestinationUserNameProvider
import org.springframework.stereotype.Component

@Component
class RtmEventHandlerMethodReturnValueHandler(private val beanFactory: BeanFactory) :
    HandlerMethodReturnValueHandler {
  private val template: SimpMessagingTemplate by lazy { beanFactory.getBean() }

  override fun supportsReturnType(type: MethodParameter): Boolean =
      type.hasMethodAnnotation(RelayMapping::class.java)

  override fun handleReturnValue(value: Any?, type: MethodParameter, message: Message<*>) {
    val payload =
        RtmEvent(
            type.getMethodAnnotation(RelayMapping::class.java)?.type ?: return,
            value,
        )

    val principal = SimpMessageHeaderAccessor.getUser(message.headers) ?: return

    val username =
        if (principal is DestinationUserNameProvider) principal.destinationUserName
        else principal.name

    val headers =
        SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE).apply {
          template.headerInitializer?.initHeaders(this)

          sessionId = SimpMessageHeaderAccessor.getSessionId(message.headers)

          setHeader(SimpMessagingTemplate.CONVERSION_HINT_HEADER, type)

          setLeaveMutable(true)
        }

    template.convertAndSendToUser(username, "/queue/replies", payload, headers.messageHeaders)
  }
}
