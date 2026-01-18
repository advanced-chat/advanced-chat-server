package org.advancedchat.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/")
class MainController {
  @GetMapping
  fun index(): String {
    return "index"
  }

  @GetMapping("/{path:[^.]*}")
  fun any(@PathVariable path: String?): String {
    return "index"
  }
}
