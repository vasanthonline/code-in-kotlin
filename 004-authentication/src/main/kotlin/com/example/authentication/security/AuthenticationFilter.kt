package com.example.authentication.security

import com.example.authentication.constants.AppConstants
import com.example.authentication.model.User
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
import com.example.authentication.model.UserSession
import com.example.authentication.service.UserService
import com.example.authentication.service.UserSessionService

import java.util.ArrayList

import org.springframework.security.core.context.SecurityContextHolder

import java.io.UnsupportedEncodingException
import java.net.URLEncoder


class AuthenticationFilter(
    authenticationManager: AuthenticationManager,
    private val jwtUtils: JwtUtils,
    private val userService: UserService,
    private val userSessionService: UserSessionService
) : UsernamePasswordAuthenticationFilter(authenticationManager) {

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        val body = StringBuffer()
        try {
            val reader = request.reader
            while (true) {
                val line = reader.readLine() ?: break
                body.append(line)
            }
            val jsonObject = JSONObject(body.toString())
            val email = jsonObject.getString(AppConstants.EMAIL)
            val password = jsonObject.getString(AppConstants.PASSWORD)
            if (email == null || password == null || email.isBlank() || password.isBlank()) {
                throw AuthenticationCredentialsNotFoundException("Credentials not found")
            }

            return authenticationManager.authenticate(UsernamePasswordAuthenticationToken(email, password, ArrayList()))
        } catch (ex: IOException) {
            logger.error(ex.message, ex)
            throw AuthenticationCredentialsNotFoundException("Request " + "reading" + " error", ex)
        }
    }

    override fun successfulAuthentication(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain, authentication: Authentication) {
        val email: String = (authentication.principal as org.springframework.security.core.userdetails.User).username
        val jwtToken = jwtUtils.generateToken(email)

        val user: User = userService.getUserByEmail(email) ?: return

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(user, null, ArrayList())

        val session = UserSession(user = user, token = jwtToken, status = AppConstants.UserSessionStatus.ACTIVE, expiry = jwtUtils.tokenExpiryDate())
        userSessionService.saveSession(session)
        val sessionCookie = createCookieFromToken(jwtToken)

        response.addCookie(sessionCookie)
    }

    private fun createCookieFromToken(jwtToken: String): Cookie? {
        var bearerToken: String? = null
        try {
            bearerToken = URLEncoder.encode(
                AppConstants.AUTH_TYPE_BEARER + AppConstants.CHAR_WHITESPACE + jwtToken,
                "UTF-8"
            )
        } catch (e: UnsupportedEncodingException) {
            logger.error(e.message)
        }
        if (bearerToken == null) return null
        val cookie = Cookie(AppConstants.COOKIE_AUTHORIZATION, bearerToken)
        cookie.path = "/"
        return cookie
    }
}