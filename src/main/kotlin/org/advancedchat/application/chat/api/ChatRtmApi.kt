package org.advancedchat.application.chat.api

import org.advancedchat.application.chat.model.Chat
import org.advancedchat.application.chat.service.ChatService
import org.advancedchat.core.domain.api.rtm.RelayMapping
import org.advancedchat.core.domain.api.rtm.RtmApi
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.PagedModel
import org.springframework.messaging.simp.annotation.SubscribeMapping

@RtmApi
class ChatRtmApi(
    private val service: ChatService,
    private val assembler: ChatRepresentationAssembler,
) {
  @RelayMapping("user.chats.relayed")
  @SubscribeMapping("/chats.relay")
  fun getChats(
      pageable: Pageable,
      assembler: PagedResourcesAssembler<Chat>,
  ): PagedModel<ChatRepresentation> =
      assembler.toModel(service.retrieveChats(pageable), this.assembler)
}
