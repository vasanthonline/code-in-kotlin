package com.example.eventsourcing.controllers

import com.example.eventsourcing.dto.AccountCreateDTO
import com.example.eventsourcing.dto.AccountTransactionDTO
import com.example.eventsourcing.services.AccountCommandService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping(value = ["/bank-accounts"])
class AccountCommandController @Autowired constructor(private val accountCommandService: AccountCommandService) {
    @PostMapping
    fun createAccount(@RequestBody accountCreateDTO: AccountCreateDTO): String {
        return accountCommandService.createAccount(accountCreateDTO)
    }

    @PutMapping(value = ["/credit/{accountNumber}"])
    fun creditIntoAccount(@PathVariable(value = "accountNumber") accountNumber: String, @RequestBody accountTransactionDTO: AccountTransactionDTO) {
        return accountCommandService.creditIntoAccount(accountNumber, accountTransactionDTO)
    }

    @PutMapping(value = ["/debit/{accountNumber}"])
    fun debitFromAccount(@PathVariable(value = "accountNumber") accountNumber: String, @RequestBody accountTransactionDTO: AccountTransactionDTO) {
        return accountCommandService.debitFromAccount(accountNumber, accountTransactionDTO)
    }
}