package com.example

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.util.StdDateFormat
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

object Constants {
    val jsonMapper = jacksonObjectMapper().apply {
        disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        dateFormat = StdDateFormat()
    }
    const val personsTopic = "persons"

    const val personsAvroTopic = "persons-avro"

    const val kafkaBrokerUrl = "localhost:9092"

    const val schemaRegistryUrl = "http://localhost:8081"
}