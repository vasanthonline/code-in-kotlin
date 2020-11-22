package com.example.properties

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class PropertiesApplicationTests @Autowired constructor(
		private val exampleController: ExampleController
) {

	@Test
	fun loadPropertiesValueTest() {
		assertEquals(exampleController.hello(), "This is a message sample from properties file.")
	}

	@Test
	fun loadPropertiesWiredTest() {
		assertEquals(exampleController.helloWired(), "This is a message sample from properties file.")
	}

}
