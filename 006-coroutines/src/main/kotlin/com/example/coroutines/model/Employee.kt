package com.example.coroutines.model

import javax.persistence.*

@Entity
@Table(name = "employee")
data class Employee(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int = 0,

    val firstName: String = "",
    val lastName: String = ""
)
