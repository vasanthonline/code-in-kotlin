package com.example.authentication

import com.example.authentication.controller.AuthController
import com.example.authentication.dto.UserDTO
import com.example.authentication.service.DatabaseCleanupService
import com.example.authentication.service.UserService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class AuthControllerTests @Autowired constructor(
        private val authController: AuthController,
) {
    @field:Autowired
    private lateinit var truncateDatabaseService: DatabaseCleanupService

    @field:Autowired
    private lateinit var userService: UserService

    @BeforeEach
    fun cleanupBeforeEach() {
        truncateDatabaseService.truncate()
    }
    @AfterEach
    fun cleanupAfterEach() {
        truncateDatabaseService.truncate()
    }

    @Test
    fun shouldCreateNewUser(){
        val userDTO: UserDTO? = authController.signup(UserDTO(email = "example@email.com", name = "Example User"))
        assertNotNull(userDTO?.id)
        assertTrue(userDTO?.id!! > 0)
        assertNotNull(userService.getUserByEmail("example@email.com"))
    }
}