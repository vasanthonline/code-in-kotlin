package com.example.coroutines.model

import javax.persistence.*

@Entity
@Table(name = "bank_account")
data class BankAccount(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int,

    val employeeId: Int,
    val balance: Double
)
