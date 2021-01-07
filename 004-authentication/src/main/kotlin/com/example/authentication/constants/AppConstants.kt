package com.example.authentication.constants

object AppConstants {
    const val COOKIE_AUTHORIZATION: String = "Authorization"
    const val AUTH_TYPE_BEARER = "Bearer"
    const val CHAR_WHITESPACE = " "

    const val EMAIL = "email"
    const val PASSWORD = "password"

    enum class UserSessionStatus {
        ACTIVE, EXPIRED, LOGOUT
    }
}
