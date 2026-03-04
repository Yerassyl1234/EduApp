package com.example.eduapp.student

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.eduapp.feature.home.ArViewerScreen
import com.example.eduapp.feature.home.ComponentDetailScreen
import com.example.eduapp.feature.home.HomeScreen
import com.example.eduapp.feature.home.navigation.AR_VIEWER_ROUTE
import com.example.eduapp.feature.home.navigation.COMPONENT_DETAIL_ROUTE
import com.example.eduapp.feature.home.navigation.HOME_ROUTE
import com.example.eduapp.feature.home.navigation.navigateToArViewer
import com.example.eduapp.feature.profile.ProfileScreen
import com.example.eduapp.feature.profile.navigation.PROFILE_ROUTE
import com.example.eduapp.feature.teacher.admin.AdminPanelScreen
import com.example.eduapp.feature.teacher.admin.navigation.ADMIN_ROUTE
import com.example.eduapp.feature.test.TestListScreen
import com.example.eduapp.feature.test.navigation.TEST_LIST_ROUTE

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

@Composable
fun MainScreen(
    onSignOut: () -> Unit,
    onCategoryClick: (String) -> Unit,
    onTestClick: (String) -> Unit,
    onCreateTestClick: () -> Unit,
    onStudentsClick: () -> Unit,
    onCoursesClick: () -> Unit,
    homeViewModel: com.example.eduapp.feature.home.HomeViewModel = hiltViewModel()
) {
    val homeState by homeViewModel.uiState.collectAsState()
    val isTeacher = homeState.user?.role == "teacher"

    val navController = rememberNavController()

    val bottomItems = buildList {
        add(BottomNavItem(HOME_ROUTE, "Басты бет", Icons.Default.Home))
        add(BottomNavItem(TEST_LIST_ROUTE, "Тест", Icons.Default.Quiz))
        if (isTeacher) {
            add(BottomNavItem(ADMIN_ROUTE, "Админ", Icons.Default.AdminPanelSettings))
        }
        add(BottomNavItem(PROFILE_ROUTE, "Профиль", Icons.Default.Person))
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                bottomItems.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = HOME_ROUTE,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(HOME_ROUTE) {
                HomeScreen(onCategoryClick = onCategoryClick)
            }
            composable(TEST_LIST_ROUTE) {
                TestListScreen(onTestClick = onTestClick)
            }
            composable(ADMIN_ROUTE) {
                AdminPanelScreen(
                    onCreateTestClick = onCreateTestClick,
                    onTestListClick = { /* Уже на экране тестов */ },
                    onStudentsClick = onStudentsClick,
                    onCoursesClick = onCoursesClick
                )
            }
            composable(PROFILE_ROUTE) {
                ProfileScreen(onSignOut = onSignOut)
            }
            composable(COMPONENT_DETAIL_ROUTE) {
                ComponentDetailScreen(
                    onBackClick = { navController.popBackStack() },
                    onArClick = { id, title, modelFileName -> navController.navigateToArViewer(id, title, modelFileName) }
                )
            }
            composable(AR_VIEWER_ROUTE) { backStackEntry ->
                val componentId = backStackEntry.arguments?.getString("componentId") ?: ""
                val title = backStackEntry.arguments?.getString("componentTitle") ?: ""
                val modelFileName = backStackEntry.arguments?.getString("modelFileName") ?: ""
                ArViewerScreen(
                    componentId = componentId,
                    componentTitle = title,
                    modelFileName = modelFileName,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}
