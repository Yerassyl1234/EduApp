package com.example.eduapp.feature.auth

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val AUTH_ROUTE = "auth"

fun NavGraphBuilder.authScreen(onAuthSuccess: () -> Unit) {
    composable(AUTH_ROUTE) {
        AuthScreen(onAuthSuccess = onAuthSuccess)
    }
}
