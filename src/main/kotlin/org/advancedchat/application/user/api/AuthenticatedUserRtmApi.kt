package org.advancedchat.application.user.api

import org.advancedchat.application.user.service.UserService
import org.advancedchat.core.domain.api.rtm.RelayMapping
import org.advancedchat.core.domain.api.rtm.RtmApi
import org.springframework.messaging.simp.annotation.SubscribeMapping

@RtmApi
class AuthenticatedUserRtmApi(private val service: UserService) {
  @RelayMapping("user.authenticated-user.relayed")
  @SubscribeMapping("/user.relay")
  fun getUser(): AuthenticatedUserRepresentation =
      AuthenticatedUserRepresentation(service.retrieveUser("test")!!.name)
}
