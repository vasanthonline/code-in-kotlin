package com.example.authentication.security

import org.json.JSONObject
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class AuthenticationFilter(authenticationManager: AuthenticationManager, private val jwtUtils: JwtUtils) : UsernamePasswordAuthenticationFilter(authenticationManager) {

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        var line: String? = null

        val body = StringBuffer()
        try {
            val reader = request.reader
            while (reader.readLine().also { line = it } != null) {
                body.append(line)
            }
            val jsonObject = JSONObject(body.toString())
            val email = jsonObject.getString("email")
            val password = jsonObject.getString("password")
            if (email == null || password == null || email.isBlank() || password.isBlank()) {
                throw AuthenticationCredentialsNotFoundException("Credentials not found")
            }


            return authenticationManager.authenticate(UsernamePasswordAuthenticationToken(email, password, ArrayList()))
        } catch (ex: IOException) {
            logger.error(ex.message, ex)
            throw AuthenticationCredentialsNotFoundException("Request " + "reading" + " error", ex)
        }
        return authenticationManager.authenticate(UsernamePasswordAuthenticationToken(null, null, ArrayList()))
    }

    override fun successfulAuthentication(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain, authentication: Authentication) {
        val email: String = (authentication!!.principal as org.springframework.security.core.userdetails.User).username
        val jwtToken = jwtUtils.generateToken(email)
        val sessionCookie = Cookie("Authorization", jwtToken)
        response!!.addCookie(sessionCookie)
    }
}