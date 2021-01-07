package com.example.authentication.security

import com.example.authentication.constants.AppConstants
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.util.*


@Component
class JwtUtils {

    @Value("\${jwt.token_expiry}")
    lateinit var tokenExpiry: String

    @Value("\${jwt.secret}")
    lateinit var secret: String

    fun parseToken(bearerToken: String): String? {
        val token = parseBearerToken(bearerToken)
        return try {
            val body = Jwts.parser()
                    .setSigningKey(secret.encodeToByteArray())
                    .parseClaimsJws(token)
                    .body

            return body.subject
        } catch (e: JwtException) {
            null
        } catch (e: ClassCastException) {
            null
        }
    }

    fun parseBearerToken(bearerToken: String): String? {
        try {
            val decodedToken = URLDecoder.decode(bearerToken, "UTF-8")
            return decodedToken.replace(AppConstants.AUTH_TYPE_BEARER + AppConstants.CHAR_WHITESPACE, "")
        } catch (e: UnsupportedEncodingException) {
            System.err.println(e.message)
        }
        return null
    }

    fun generateToken(email: String): String {
        val expiry: Long = System.currentTimeMillis() + 864000000
        return Jwts.builder().setSubject(email).setExpiration(Date(expiry))
                .signWith(SignatureAlgorithm.HS512, secret.encodeToByteArray()).compact()
    }

    fun tokenExpiryDate(): Date {
        return Date(System.currentTimeMillis() + java.lang.Long.valueOf(tokenExpiry))
    }
}