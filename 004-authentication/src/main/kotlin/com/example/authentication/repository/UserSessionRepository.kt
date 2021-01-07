package com.example.authentication.repository

import com.example.authentication.constants.AppConstants
import com.example.authentication.model.UserSession
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import java.util.*
import javax.transaction.Transactional


interface UserSessionRepository : CrudRepository<UserSession, Long> {

    @Modifying(flushAutomatically = true, clearAutomatically = true )
    @Query(value = "update user_sessions us set us.status = ?1 where us.token = ?2", nativeQuery = true)
    fun updateStatus(@Param("token") token: String, @Param("status") status: String)

    fun findFirstByStatusAndExpiryGreaterThanAndTokenOrderByUpdatedAtDesc(
        status: AppConstants.UserSessionStatus,
        expiry: Date,
        token: String
    ): UserSession?
}