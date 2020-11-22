package com.example.crud

import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "customer")
data class Customer (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0,

    var name: String = "",
    var dateOfBirth: LocalDate? = null
)
