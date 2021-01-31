package com.example.json

import com.example.Constants.jsonMapper
import com.example.Constants.personsTopic
import com.example.Person
import com.github.javafaker.Faker
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import org.apache.log4j.LogManager
import java.util.*

fun main(args: Array<String>) {
    SimpleProducer("localhost:9092").produce(2)
}

class SimpleProducer(brokers: String) {

    private val logger = LogManager.getLogger(javaClass)
    private val producer = createProducer(brokers)

    private fun createProducer(brokers: String): Producer<String, String> {
        val props = Properties()
        props["bootstrap.servers"] = brokers
        props["key.serializer"] = StringSerializer::class.java.canonicalName
        props["value.serializer"] = StringSerializer::class.java.canonicalName
        return KafkaProducer<String, String>(props)
    }

    fun produce(ratePerSecond: Int) {
        val waitTimeBetweenIterationsMs = 1000L / ratePerSecond
        logger.info("Producing $ratePerSecond records per second (1 every ${waitTimeBetweenIterationsMs}ms)")


        val faker = Faker()
        while (true) {
            val fakePerson = Person(
                firstName = faker.name().firstName(),
                lastName = faker.name().lastName(),
                birthDate = faker.date().birthday()
            )
            logger.info("Generated a person: $fakePerson")

            val fakePersonJson = jsonMapper.writeValueAsString(fakePerson)
            logger.debug("JSON data: $fakePersonJson")

            val futureResult = producer.send(ProducerRecord(personsTopic, fakePersonJson))
            logger.debug("Sent a record")

            Thread.sleep(waitTimeBetweenIterationsMs)

            // wait for the write acknowledgment
            futureResult.get()
        }
    }
}