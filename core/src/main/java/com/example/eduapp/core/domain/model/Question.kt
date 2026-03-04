package com.example.eduapp.core.domain.model

data class Question(
    val id: String = "",
    val testId: String = "",
    val text: String = "",
    val options: List<String> = emptyList(),
    val correctAnswerIndex: Int = 0
)