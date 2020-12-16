package com.example.authentication.service

import com.example.authentication.dto.UserDTO
import com.example.authentication.model.User
import com.example.authentication.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.ZoneOffset

@Service
class UserService @Autowired constructor(
        private val userRepository: UserRepository
) {
    fun getPersons(): List<User> {
        return userRepository.findAll().toList()
    }

    fun getUserByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }

    fun saveUser(user: User): User {
        return userRepository.save(user)
    }

    fun deleteUser(id: Long): Unit {
        return userRepository.deleteById(id)
    }

    fun convertToDTO(user: User): UserDTO {
        return UserDTO(user.id, user.email, user.name)
    }

    fun convertToModel(userDTO: UserDTO, password: String): User {
        return User(userDTO.id, userDTO.email, userDTO.name, password)
    }
}
