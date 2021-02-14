package com.example.coroutines.repository

import com.example.coroutines.model.BankAccount
import org.springframework.data.repository.CrudRepository

interface BankAccountRepository : CrudRepository<BankAccount, Long> {

}