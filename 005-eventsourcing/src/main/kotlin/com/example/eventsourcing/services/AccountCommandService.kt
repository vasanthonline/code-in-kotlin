package com.example.eventsourcing.services

import com.example.eventsourcing.constants.AppConstants
import com.example.eventsourcing.dto.AccountCreateDTO
import com.example.eventsourcing.dto.AccountTransactionDTO
import com.example.eventsourcing.models.Activity
import com.example.eventsourcing.repository.EventStoreRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AccountCommandService @Autowired constructor(private val eventStoreRepository: EventStoreRepository) {
    fun createAccount(accountCreateDTO: AccountCreateDTO): String {
        val createAccountActivity = Activity(activityType = AppConstants.ActivityType.CREATE_ACCOUNT,  accountId = UUID.randomUUID().toString(), amount = accountCreateDTO.startingBalance)
        val createdActivity = eventStoreRepository.save(createAccountActivity)
        return createdActivity.accountId
    }

    fun creditIntoAccount(accountId: String, accountTransactionDTO: AccountTransactionDTO) {
        val createAccountActivity = Activity(activityType = AppConstants.ActivityType.CREDIT_TO_ACCOUNT,  accountId = accountId, amount = accountTransactionDTO.amount)
        eventStoreRepository.save(createAccountActivity)
    }

    fun debitFromAccount(accountId: String, accountTransactionDTO: AccountTransactionDTO) {
        val createAccountActivity = Activity(activityType = AppConstants.ActivityType.DEBIT_FROM_ACCOUNT,  accountId = accountId, amount = accountTransactionDTO.amount)
        eventStoreRepository.save(createAccountActivity)
    }
}