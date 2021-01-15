package com.example.eventsourcing.models

import com.example.eventsourcing.constants.AppConstants
import org.springframework.data.annotation.CreatedDate
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "activities")
data class Activity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Enumerated(value = EnumType.STRING)
    val activityType: AppConstants.ActivityType = AppConstants.ActivityType.GET_ACCOUNT,

    val accountId: String = "",

    val amount: Double = 0.0,

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    var createdAt: Date = Date()

)
