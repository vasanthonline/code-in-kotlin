package com.example.authentication

import com.example.authentication.constants.AppConstants
import com.example.authentication.controller.AuthController
import com.example.authentication.dto.UserDTO
import com.example.authentication.model.User
import com.example.authentication.model.UserSession
import com.example.authentication.service.DatabaseCleanupService
import com.example.authentication.service.UserService
import com.example.authentication.service.UserSessionService
import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.text.IsEmptyString.emptyOrNullString
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class AuthControllerTests @Autowired constructor(
    private val authController: AuthController,
    private val truncateDatabaseService: DatabaseCleanupService,
    private val userService: UserService,
    private val userSessionService: UserSessionService,
    private val mvc: MockMvc
) {

    @BeforeEach
    fun cleanupBeforeEach() {
        truncateDatabaseService.truncate()
    }
    @AfterEach
    fun cleanupAfterEach() {
        truncateDatabaseService.truncate()
    }

    @Test
    fun shouldCreateNewUser() {
        val userDTO: UserDTO? = authController.signup(UserDTO(email = "example@email.com", name = "Example User"))
        assertNotNull(userDTO?.id)
        assertTrue(userDTO?.id!! > 0)
        val user: User? = userService.getUserByEmail("example@email.com")
        assertNotNull(user)
        assertTrue(user?.id!! > 0)
        assertEquals("Example User", user.name)
    }

    @Test
    fun shouldLoginUser() {
        val userDTO: UserDTO? = authController.signup(UserDTO(email = "example@email.com", name = "Example User"))
        val user:User? = userService.getUserByEmail(userDTO?.email ?: "")
        val resultActions: ResultActions = mvc.perform(
            MockMvcRequestBuilders
                .post("/login")
                .content(ObjectMapper().writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().is2xxSuccessful)
        .andExpect(cookie().exists(AppConstants.COOKIE_AUTHORIZATION))

        resultActions.andDo {
            val bearerToken: String? = it.response.getCookie(AppConstants.COOKIE_AUTHORIZATION)?.value
            val token: String? = userService.getTokenFromBearerToken(bearerToken ?: "")
            val userSession: UserSession? = userSessionService.findActiveSession(token ?: "")
            assertNotNull(userSession)
            assertEquals(userSession?.token, token)
        }
    }

    @Test
    fun shouldLoginAndFetchProfileOfUser() {
        val userDTO: UserDTO? = authController.signup(UserDTO(email = "example@email.com", name = "Example User"))
        val user:User? = userService.getUserByEmail(userDTO?.email ?: "")
        val resultActions: ResultActions = mvc.perform(
            MockMvcRequestBuilders
                .post("/login")
                .content(ObjectMapper().writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().is2xxSuccessful)
            .andExpect(cookie().exists(AppConstants.COOKIE_AUTHORIZATION))

        resultActions.andDo {
            mvc.perform(MockMvcRequestBuilders
                .get("/user/profile")
                .cookie(it.response.getCookie(AppConstants.COOKIE_AUTHORIZATION))
            )
            .andExpect(status().is2xxSuccessful)
            .andExpect(content().json("{\"id\":" + user?.id + ",\"email\":\"example@email.com\",\"name\":\"Example User\"}"))
        }
    }

    @Test
    fun shouldLogoutUser() {
        val userDTO: UserDTO? = authController.signup(UserDTO(email = "example@email.com", name = "Example User"))
        val user:User? = userService.getUserByEmail(userDTO?.email ?: "")
        val resultActions: ResultActions = mvc.perform(
            MockMvcRequestBuilders
                .post("/login")
                .content(ObjectMapper().writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().is2xxSuccessful)
        .andExpect(cookie().exists(AppConstants.COOKIE_AUTHORIZATION))
        .andExpect(cookie().maxAge(AppConstants.COOKIE_AUTHORIZATION, -1))

        resultActions.andDo {
            mvc.perform(MockMvcRequestBuilders
                .get("/user/logout")
                .cookie(it.response.getCookie(AppConstants.COOKIE_AUTHORIZATION))
            )
            .andExpect(status().is2xxSuccessful)
            .andExpect(cookie().maxAge(AppConstants.COOKIE_AUTHORIZATION, 0))
            .andExpect(cookie().value(AppConstants.COOKIE_AUTHORIZATION, emptyOrNullString()))

            mvc.perform(MockMvcRequestBuilders
                .get("/user/profile")
            )
            .andExpect(status().is4xxClientError)
        }
    }
}