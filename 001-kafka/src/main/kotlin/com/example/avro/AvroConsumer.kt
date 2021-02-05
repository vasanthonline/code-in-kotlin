package com.example.avro

import com.example.Constants.kafkaBrokerUrl
import com.example.Constants.personsAvroTopic
import com.example.Constants.schemaRegistryUrl
import com.example.Person
import io.confluent.kafka.serializers.KafkaAvroDeserializer
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.log4j.LogManager
import java.time.Duration
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId
import java.util.*


fun main() {
    AvroConsumer(kafkaBrokerUrl, schemaRegistryUrl).process()
}

class AvroConsumer(brokers: String, schemaRegistryUrl: String) {
    private val logger = LogManager.getLogger(javaClass)
    private val consumer = createConsumer(brokers, schemaRegistryUrl)

    private fun createConsumer(brokers: String, schemaRegistryUrl: String): Consumer<String, GenericRecord> {
        val props = Properties()
        props["bootstrap.servers"] = brokers
        props["group.id"] = "person-processor"
        props["key.deserializer"] = StringDeserializer::class.java
        props["value.deserializer"] = KafkaAvroDeserializer::class.java
        props["schema.registry.url"] = schemaRegistryUrl
        return KafkaConsumer(props)
    }

    fun process() {
        consumer.subscribe(listOf(personsAvroTopic))

        logger.info("Consuming and processing data")

        while (true) {
            val records = consumer.poll(Duration.ofSeconds(1))
            logger.info("Received ${records.count()} records")

            records.iterator().forEach {
                val personAvro = it.value()

                val person = Person(
                    firstName = personAvro["firstName"].toString(),
                    lastName = personAvro["lastName"].toString(),
                    birthDate = Date(personAvro["birthDate"] as Long)
                )
                logger.debug("Person: $person")

                val birthDateLocal = person.birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                val age = Period.between(birthDateLocal, LocalDate.now()).years
                logger.debug("Age: $age")
            }
        }
    }
}