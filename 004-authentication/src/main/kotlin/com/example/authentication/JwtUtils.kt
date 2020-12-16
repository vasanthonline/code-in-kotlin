package com.example.authentication

import com.example.authentication.model.User
import com.example.authentication.service.UserService
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtUtils {
    @Value("\${jwt.secret}")
    lateinit var secret: String

    @Autowired
    lateinit var userService: UserService

    fun parseToken(token: String?): User? {
        return try {
            val body = Jwts.parser()
                    .setSigningKey(secret.encodeToByteArray())
                    .parseClaimsJws(token)
                    .body

            return userService.getUserByEmail(body.subject)
        } catch (e: JwtException) {
            null
        } catch (e: ClassCastException) {
            null
        }
    }

    fun generateToken(email: String): String {
        val expiry: Long = System.currentTimeMillis() + 864000000
        return Jwts.builder().setSubject(email).setExpiration(Date(expiry))
                .signWith(SignatureAlgorithm.HS512, secret.encodeToByteArray()).compact()
    }
}