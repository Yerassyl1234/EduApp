package com.example.eduapp.core.domain.model

data class HelpRequest(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val message: String = "",
    val sentAt: Long = 0L
)