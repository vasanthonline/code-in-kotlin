package com.example.authentication.model

import com.example.authentication.constants.AppConstants
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "user_sessions")
data class UserSession(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user:User? = null,

    val token: String = "",

    @Enumerated(EnumType.STRING)
    var status: AppConstants.UserSessionStatus = AppConstants.UserSessionStatus.EXPIRED,

    val expiry: Date = Date(),

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    var createdAt: Date = Date(),

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    var updatedAt: Date = Date()
)
