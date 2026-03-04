package com.example.eduapp.feature.profile.navigation


import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.eduapp.feature.profile.ProfileScreen

const val PROFILE_ROUTE = "profile"

fun NavGraphBuilder.profileScreen(onSignOut: () -> Unit) {
    composable(PROFILE_ROUTE) {
        ProfileScreen(onSignOut = onSignOut)
    }
}
