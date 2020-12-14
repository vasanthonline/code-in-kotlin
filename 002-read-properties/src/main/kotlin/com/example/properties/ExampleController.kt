package com.example.properties

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.GetMapping

@RestController
class ExampleController {

  @Value("\${example.properties.message}")
  lateinit var message: String

  @Autowired
  lateinit var appPropertiesWired: AppPropertiesWired

  @GetMapping("/")
  fun hello(): String {
    return message
  }

  @GetMapping("/wiredProperties")
  fun helloWired(): String {
    return appPropertiesWired.exampleProperties.message
  }

}