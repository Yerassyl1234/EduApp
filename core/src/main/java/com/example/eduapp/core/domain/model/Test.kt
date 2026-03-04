package com.example.eduapp.core.domain.model

data class Test(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val categoryId: String = "",
    val imageUrl: String = "",
    val createdBy: String = "",
    val questionCount: Int = 0
)