package com.example.eventsourcing

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EventsourcingApplication

fun main(args: Array<String>) {
	runApplication<EventsourcingApplication>(*args)
}
