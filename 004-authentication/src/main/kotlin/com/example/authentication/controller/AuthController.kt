package com.example.authentication.controller

import com.example.authentication.JwtUtils
import com.example.authentication.dto.UserDTO
import com.example.authentication.model.User
import com.example.authentication.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping(path = ["/user"])
class AuthController {

    @Autowired
    lateinit var jwtUtils: JwtUtils

    @Autowired
    lateinit var userService: UserService

    @PostMapping(path = ["/signup"])
    @ResponseBody
    fun signup(@RequestBody userDTO: UserDTO): UserDTO? {
        val generatedPassword: String = UUID.randomUUID().toString().substring(0,10)
        val user: User = userService.convertToModel(userDTO, generatedPassword)
        return userService.convertToDTO(userService.saveUser(user))
    }

    @GetMapping(path = ["/profile"])
    @ResponseBody
    fun profile(@CookieValue(name= "Authorization") bearerToken: String): UserDTO? {
        val user : User = jwtUtils.parseToken(bearerToken) ?: throw Exception("Unauthrized call!")
        return userService.convertToDTO(user)
    }
}