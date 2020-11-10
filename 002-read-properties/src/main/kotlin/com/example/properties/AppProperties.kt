package com.example.properties

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class AppProperties {
    @Value("\${example.properties.message}")
    lateinit var message: String
}