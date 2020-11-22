package com.example.properties

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.boot.context.properties.ConfigurationProperties;

@Component
@ConfigurationProperties(prefix="example")
data class AppPropertiesWired (
    var properties: ExamplePropertiesWired
)

@Component
@ConfigurationProperties(prefix="properties")
data class ExamplePropertiesWired (
    var message: String = ""
)