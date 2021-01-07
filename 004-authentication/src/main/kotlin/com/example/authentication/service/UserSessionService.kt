package com.example.authentication.service

import com.example.authentication.constants.AppConstants
import com.example.authentication.model.UserSession
import com.example.authentication.repository.UserSessionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import javax.servlet.http.Cookie

@Service
class UserSessionService @Autowired constructor(
    private val userSessionRepository: UserSessionRepository
)  {

    fun saveSession(session: UserSession): UserSession? {
        return userSessionRepository.save(session)
    }

    fun logout(token: String): Cookie {
        val cookie = Cookie(AppConstants.COOKIE_AUTHORIZATION, null)
        cookie.path = "/"
        cookie.maxAge = 0

        val userSession: UserSession = findActiveSession(token)
            ?: return cookie
        userSession.status = AppConstants.UserSessionStatus.LOGOUT
        saveSession(userSession)
        return cookie
    }

    fun isActiveSession(token: String): Boolean {
        val userSession = findActiveSession(token)
        return userSession != null
    }

    fun findActiveSession(token: String): UserSession? {
        return userSessionRepository.findFirstByStatusAndExpiryGreaterThanAndTokenOrderByUpdatedAtDesc(
            AppConstants.UserSessionStatus.ACTIVE, Date(),
            token
        )
    }
}