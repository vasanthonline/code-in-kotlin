package com.example.properties

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RestController
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping

@RestController
class HtmlController {

  @Autowired
  lateinit var appProperties: AppProperties

  @GetMapping("/")
  fun hello(model: Model): String {
    return appProperties.message
  }

}