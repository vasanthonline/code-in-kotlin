package com.example.eventsourcing.controllers

import com.example.eventsourcing.dto.AccountQueryDTO
import com.example.eventsourcing.models.Activity
import com.example.eventsourcing.services.AccountQueryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping(value = ["/bank-account"])
class AccountQueryController @Autowired constructor(private val accountQueryService: AccountQueryService) {

    @GetMapping(value = ["/{accountNumber}"])
    fun getAccount(@PathVariable(value = "accountNumber") accountNumber: String): AccountQueryDTO {
        return accountQueryService.getAccount(accountNumber)
    }

    @GetMapping(value = ["/{accountNumber}/transactions"])
    fun getAccountTransactions(@PathVariable(value = "accountNumber") accountNumber: String): Array<Activity> {
        return accountQueryService.getAccountEvents(accountNumber)
    }
}