package com.example.crud

import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "person")
data class Person (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0,

    var name: String = "",
    var dateOfBirthInMs: Long = 0
)
