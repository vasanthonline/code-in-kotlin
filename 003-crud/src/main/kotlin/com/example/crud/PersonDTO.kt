package com.example.crud

import java.time.LocalDate

data class PersonDTO(val id: Long = 0, val name: String, val dateOfBirth: LocalDate)