package org.advancedchat.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication class AdvancedChatServerApplication

fun main(args: Array<String>) {
  runApplication<AdvancedChatServerApplication>(*args)
}
