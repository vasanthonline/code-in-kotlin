package com.example.eventsourcing.services

import com.example.eventsourcing.constants.AppConstants
import com.example.eventsourcing.dto.AccountQueryDTO
import com.example.eventsourcing.models.Activity
import com.example.eventsourcing.repository.EventStoreRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AccountQueryService @Autowired constructor(private val eventStoreRepository: EventStoreRepository) {
    fun getAccount(accountId: String): AccountQueryDTO {
        val activities = eventStoreRepository.findByAccountIdOrderByCreatedAtAsc(accountId)
        val accountBalance = activities.fold(0.0) { balance, activity ->
            val updatedBalance = (when(activity.activityType) {
                AppConstants.ActivityType.CREATE_ACCOUNT, AppConstants.ActivityType.CREDIT_TO_ACCOUNT -> balance + activity.amount
                AppConstants.ActivityType.DEBIT_FROM_ACCOUNT -> balance - activity.amount
                else -> balance
            })
            updatedBalance
        }
        return AccountQueryDTO(accountId, accountBalance)
    }

    fun getAccountEvents(accountId: String): Array<Activity> {
        return eventStoreRepository.findByAccountIdOrderByCreatedAtAsc(accountId)
    }
}