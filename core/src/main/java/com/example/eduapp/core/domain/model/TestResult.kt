package com.example.eduapp.core.domain.model

data class TestResult(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val testId: String = "",
    val testTitle: String = "",
    val score: Int = 0,
    val totalQuestions: Int = 0,
    val answeredAt: Long = System.currentTimeMillis(),
    val published: Boolean = false
)