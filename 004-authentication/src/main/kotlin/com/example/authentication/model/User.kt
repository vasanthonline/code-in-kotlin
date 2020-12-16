package com.example.authentication.model

import javax.persistence.*

@Entity
@Table(name = "users")
data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = 0,

        val email: String = "",
        val name: String = "",
        val password: String = ""
)

