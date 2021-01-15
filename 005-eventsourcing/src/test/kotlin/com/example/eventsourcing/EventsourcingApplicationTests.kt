package com.example.eventsourcing

import com.example.eventsourcing.constants.AppConstants
import com.example.eventsourcing.controllers.AccountCommandController
import com.example.eventsourcing.controllers.AccountQueryController
import com.example.eventsourcing.dto.AccountCreateDTO
import com.example.eventsourcing.dto.AccountTransactionDTO
import com.example.eventsourcing.services.DatabaseCleanupService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class EventsourcingApplicationTests @Autowired constructor(
	private val accountCommandController: AccountCommandController,
	private val accountQueryController: AccountQueryController,
	private val truncateDatabaseService: DatabaseCleanupService,
) {
	@BeforeEach
	fun cleanupBeforeEach() {
		truncateDatabaseService.truncate()
	}
	@AfterEach
	fun cleanupAfterEach() {
		truncateDatabaseService.truncate()
	}

	@Test
	fun shouldCreateAccount() {
		val accountCreateDTO = AccountCreateDTO(10.0)
		val accountId = accountCommandController.createAccount(accountCreateDTO)
		assertNotNull(accountId)
		val transactions = accountQueryController.getAccountTransactions(accountId)
		assertEquals(1, transactions.size)
		assertEquals(accountId, transactions[0].accountId)
		assertEquals(AppConstants.ActivityType.CREATE_ACCOUNT, transactions[0].activityType)
		assertEquals(10.0, transactions[0].amount)
	}

	@Test
	fun shouldCreditIntoAccount() {
		val accountCreateDTO = AccountCreateDTO(10.0)
		val accountId = accountCommandController.createAccount(accountCreateDTO)
		accountCommandController.creditIntoAccount(accountId, AccountTransactionDTO(15.0))

		val transactions = accountQueryController.getAccountTransactions(accountId)
		assertEquals(2, transactions.size)
		assertEquals(accountId, transactions[1].accountId)
		assertEquals(AppConstants.ActivityType.CREDIT_TO_ACCOUNT, transactions[1].activityType)
		assertEquals(15.0, transactions[1].amount)
	}

	@Test
	fun shouldDebitFromAccount() {
		val accountCreateDTO = AccountCreateDTO(25.0)
		val accountId = accountCommandController.createAccount(accountCreateDTO)
		accountCommandController.debitFromAccount(accountId, AccountTransactionDTO(10.0))

		val transactions = accountQueryController.getAccountTransactions(accountId)
		assertEquals(2, transactions.size)
		assertEquals(accountId, transactions[1].accountId)
		assertEquals(AppConstants.ActivityType.DEBIT_FROM_ACCOUNT, transactions[1].activityType)
		assertEquals(10.0, transactions[1].amount)
	}

	@Test
	fun shouldListAccountEvents() {
		val accountCreateDTO = AccountCreateDTO(25.0)
		val accountId = accountCommandController.createAccount(accountCreateDTO)
		accountCommandController.creditIntoAccount(accountId, AccountTransactionDTO(100.0))
		accountCommandController.debitFromAccount(accountId, AccountTransactionDTO(50.0))
		accountCommandController.debitFromAccount(accountId, AccountTransactionDTO(10.0))
		accountCommandController.creditIntoAccount(accountId, AccountTransactionDTO(100.0))

		val account = accountQueryController.getAccount(accountId)
		assertEquals(165.0, account.balance)
		assertEquals(accountId, account.id)
	}

}
