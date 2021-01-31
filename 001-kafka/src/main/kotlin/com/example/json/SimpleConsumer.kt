package com.example.json

import com.example.Constants.jsonMapper
import com.example.Constants.personsTopic
import com.example.Person
import com.fasterxml.jackson.module.kotlin.readValue
import io.confluent.kafka.serializers.KafkaAvroDeserializer
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.log4j.LogManager
import java.time.Duration
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId
import java.util.*


fun main(args: Array<String>) {
    SimpleConsumer("localhost:9092", "http://localhost:8081").process()
}

class SimpleConsumer(brokers: String, schemaRegistryUrl: String) {
    private val logger = LogManager.getLogger(javaClass)
    private val consumer = createConsumer(brokers, schemaRegistryUrl)

    private fun createConsumer(brokers: String, schemaRegistryUrl: String): Consumer<String, String> {
        val props = Properties()
        props["bootstrap.servers"] = brokers
        props["group.id"] = "person-processor"
        props["key.deserializer"] = StringDeserializer::class.java
        props["value.deserializer"] = StringDeserializer::class.java
        props["schema.registry.url"] = schemaRegistryUrl
        return KafkaConsumer(props)
    }

    fun process() {
        consumer.subscribe(listOf(personsTopic))

        logger.info("Consuming and processing data")

        while (true) {
            val records = consumer.poll(Duration.ofSeconds(1))
            logger.info("Received ${records.count()} records")

            records.iterator().forEach {
                val personJson = it.value()
                logger.debug("JSON data: $personJson")

                val person = jsonMapper.readValue<Person>(personJson)
                logger.debug("Person: $person")

                val birthDateLocal = person.birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                val age = Period.between(birthDateLocal, LocalDate.now()).years
                logger.debug("Age: $age")
            }
        }
    }
}