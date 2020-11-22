package com.example.crud

import java.time.LocalDate

data class CustomerDTO(val id: Long = 0, val name: String, val ageInDays: Long? = 0)