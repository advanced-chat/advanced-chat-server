package org.advancedchat.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication class AdvancedChatServer

fun main(args: Array<String>) {
  runApplication<AdvancedChatServer>(*args)
}
