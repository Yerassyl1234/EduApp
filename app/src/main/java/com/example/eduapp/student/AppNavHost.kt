package com.example.eduapp.student

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.eduapp.core.data.repository.AuthRepository
import com.example.eduapp.feature.auth.AUTH_ROUTE
import com.example.eduapp.feature.auth.authScreen
import com.example.eduapp.feature.home.navigation.*
import com.example.eduapp.feature.test.navigation.*
import com.example.eduapp.feature.teacher.admin.navigation.*

const val SPLASH_ROUTE = "splash"

@Composable
fun AppNavHost(authRepository: AuthRepository) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = SPLASH_ROUTE) {

        composable(SPLASH_ROUTE) {
            SplashScreen(
                authRepository = authRepository,
                onNavigateToMain = {
                    navController.navigate("main") {
                        popUpTo(SPLASH_ROUTE) { inclusive = true }
                    }
                },
                onNavigateToAuth = {
                    navController.navigate(AUTH_ROUTE) {
                        popUpTo(SPLASH_ROUTE) { inclusive = true }
                    }
                }
            )
        }

        authScreen(
            onAuthSuccess = {
                navController.navigate("main") {
                    popUpTo(AUTH_ROUTE) { inclusive = true }
                }
            }
        )

        composable("main") {
            MainScreen(
                onSignOut = {
                    navController.navigate(AUTH_ROUTE) {
                        popUpTo("main") { inclusive = true }
                    }
                },
                onCategoryClick = { categoryId ->
                    navController.navigateToCategory(categoryId)
                },
                onTestClick = { testId ->
                    navController.navigateToTest(testId)
                },
                onCreateTestClick = {
                    navController.navigateToCreateTest()
                },
                onStudentsClick = {
                    navController.navigateToStudentsList()
                },
                onCoursesClick = {
                    navController.navigateToCourses()
                }
            )
        }

        categoryDetailScreen(
            onBackClick = { navController.popBackStack() },
            onComponentClick = { navController.navigateToComponent(it) }
        )

        componentDetailScreen(
            onBackClick = { navController.popBackStack() },
            onArClick = { id, title, modelFileName -> navController.navigateToArViewer(id, title, modelFileName) }
        )

        arViewerScreen(onBackClick = { navController.popBackStack() })

        testScreen(
            onBackClick = { navController.popBackStack() },
            onFinish = { navController.popBackStack() }
        )

        testResultScreen(
            onBackToTests = { navController.popBackStack() }
        )

        createTestScreen(onBackClick = { navController.popBackStack() })
        studentsListScreen(onBackClick = { navController.popBackStack() })
        coursesScreen(
            onBackClick = { navController.popBackStack() },
            onSectionClick = { sectionId ->
                navController.navigateToSectionEdit(sectionId)
            }
        )
        sectionEditScreen(onBackClick = { navController.popBackStack() })
    }
}
