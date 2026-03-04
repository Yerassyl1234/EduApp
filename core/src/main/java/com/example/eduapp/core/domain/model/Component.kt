package com.example.eduapp.core.domain.model

data class Component(
    val id: String = "",
    val categoryId: String = "",
    val title: String = "",
    val imageUrl: String = "",
    val description: String = "",
    val composition: List<String> = emptyList(),
    val function: String = "",
    val modelFileName: String = ""
)