package org.advancedchat.application.ui.api

import org.advancedchat.application.ui.model.AdvancedChatConfiguration
import org.advancedchat.application.ui.model.LoadingBarConfiguration
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ModelAttribute

@ControllerAdvice
class AdvancedChatConfigurationAdvice {
  @ModelAttribute("advancedChatConfiguration")
  fun advancedChatConfiguration(): AdvancedChatConfiguration =
      AdvancedChatConfiguration(
          LoadingBarConfiguration(
              5,
              mapOf(
                  "0" to "rgba(26,  188, 156, .7)",
                  ".3" to "rgba(41,  128, 185, .7)",
                  "1.0" to "rgba(231, 76,  60,  .7)",
              ),
              5.0,
              "rgba(0, 0, 0, .5)",
          )
      )
}
