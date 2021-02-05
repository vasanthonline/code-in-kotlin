package com.example

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.util.StdDateFormat
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

object Constants {

    const val kafkaBrokerUrl = "localhost:9092"

    const val messagesTopic = "messages"

    const val cassandraHost = "127.0.0.1"
}