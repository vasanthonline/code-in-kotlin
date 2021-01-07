package com.example.authentication.service

import com.example.authentication.dto.UserDTO
import com.example.authentication.model.User
import com.example.authentication.repository.UserRepository
import com.example.authentication.security.JwtUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService @Autowired constructor(
        private val userRepository: UserRepository,
        private val jwtUtils: JwtUtils
) {

    fun getUserByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }

    fun saveUser(user: User): User {
        return userRepository.save(user)
    }

    fun parseToken(token: String): User? {
        return getUserByEmail(jwtUtils.parseToken(token) ?: "")
    }

    fun getTokenFromBearerToken(bearerToken: String): String? {
        return jwtUtils.parseBearerToken(bearerToken)
    }

    fun convertToDTO(user: User): UserDTO {
        return UserDTO(user.id, user.email, user.name)
    }

    fun convertToModel(userDTO: UserDTO, password: String): User {
        return User(userDTO.id, userDTO.email, userDTO.name, password)
    }
}
