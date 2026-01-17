package org.advancedchat.application

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["org.advancedchat"]) class AdvancedChat

fun main(args: Array<String>) {
  runApplication<AdvancedChat>(*args)
}
