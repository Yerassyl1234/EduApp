package com.example.eduapp.core.domain.model

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val role: String = "student",
    val photoUrl: String = ""
)
