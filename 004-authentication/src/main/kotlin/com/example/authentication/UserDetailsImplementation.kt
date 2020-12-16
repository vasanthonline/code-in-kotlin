package com.example.authentication

import com.example.authentication.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.util.*


@Component
class UserDetailsImplementation : UserDetailsService {

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    lateinit var userService: UserService

    override fun loadUserByUsername(username: String?): UserDetails {
        val user: com.example.authentication.model.User? = userService.getUserByEmail(username ?: "")
        return User(user?.email, passwordEncoder.encode(user?.password), Collections.emptyList())
    }

}