package com.example.coroutines.service

import com.example.coroutines.model.BankAccount
import com.example.coroutines.model.Employee
import com.example.coroutines.repository.BankAccountRepository
import com.example.coroutines.repository.EmployeeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BankAccountService @Autowired constructor(
    private val bankAccountRepository: BankAccountRepository
)  {
    fun createBankAccount(bankAccount: BankAccount) {
        bankAccountRepository.save(bankAccount)
    }
}