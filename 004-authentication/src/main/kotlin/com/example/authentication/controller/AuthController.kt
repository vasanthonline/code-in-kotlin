package com.example.authentication.controller

import com.example.authentication.constants.AppConstants
import com.example.authentication.dto.UserDTO
import com.example.authentication.model.User
import com.example.authentication.service.UserService
import com.example.authentication.service.UserSessionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping(path = ["/user"])
class AuthController {

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var userSessionService: UserSessionService

    @PostMapping(path = ["/signup"])
    @ResponseBody
    fun signup(@RequestBody userDTO: UserDTO): UserDTO? {
        // As an example, a temporary password is generated here.
        // In real-world applications, either the user is expected to signup with the password [or]
        // the generated temporary password is shared via email.
        val generatedPassword: String = UUID.randomUUID().toString().substring(0,10)
        val user: User = userService.convertToModel(userDTO, generatedPassword)
        return userService.convertToDTO(userService.saveUser(user))
    }

    @GetMapping(path = ["/profile"])
    @ResponseBody
    fun profile(@CookieValue(name= AppConstants.COOKIE_AUTHORIZATION) bearerToken: String): UserDTO? {
        val user : User = userService.parseToken(bearerToken) ?: throw Exception("Unauthorized call!")
        return userService.convertToDTO(user)
    }

    @GetMapping(path = ["/logout"])
    @ResponseBody
    fun logout(
        @CookieValue(name = AppConstants.COOKIE_AUTHORIZATION) bearerToken: String,
        request: HttpServletRequest?,
        response: HttpServletResponse
    ) {
        val token: String = userService.getTokenFromBearerToken(bearerToken) ?: return
        val blacklistedCookie: Cookie = userSessionService.logout(token)
        response.addCookie(blacklistedCookie)
    }
}