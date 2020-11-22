package com.example.properties

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RestController
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping

@RestController
class ExampleController {

  @Autowired
  lateinit var appProperties: AppProperties

  @Autowired
  lateinit var appPropertiesWired: AppPropertiesWired

  @GetMapping("/")
  fun hello(): String {
    return appProperties.message
  }

  @GetMapping("/wiredProperties")
  fun helloWired(): String {
    return appPropertiesWired.properties.message
  }

}