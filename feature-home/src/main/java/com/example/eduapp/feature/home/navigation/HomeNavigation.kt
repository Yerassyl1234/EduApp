package com.example.eduapp.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.eduapp.feature.home.ArViewerScreen
import com.example.eduapp.feature.home.CategoryDetailScreen
import com.example.eduapp.feature.home.ComponentDetailScreen
import com.example.eduapp.feature.home.HomeScreen

const val HOME_ROUTE = "home"
const val CATEGORY_DETAIL_ROUTE = "category/{categoryId}"
const val COMPONENT_DETAIL_ROUTE = "component/{componentId}"
const val AR_VIEWER_ROUTE = "ar_viewer/{componentId}/{componentTitle}/{modelFileName}"

fun NavGraphBuilder.homeScreen(onCategoryClick: (String) -> Unit) {
    composable(HOME_ROUTE) {
        HomeScreen(onCategoryClick = onCategoryClick)
    }
}

fun NavGraphBuilder.categoryDetailScreen(
    onBackClick: () -> Unit,
    onComponentClick: (String) -> Unit
) {
    composable(CATEGORY_DETAIL_ROUTE) {
        CategoryDetailScreen(
            onBackClick = onBackClick,
            onComponentClick = onComponentClick
        )
    }
}

fun NavGraphBuilder.componentDetailScreen(
    onBackClick: () -> Unit,
    onArClick: (String, String, String) -> Unit
) {
    composable(COMPONENT_DETAIL_ROUTE) {
        ComponentDetailScreen(
            onBackClick = onBackClick,
            onArClick = onArClick
        )
    }
}

fun NavGraphBuilder.arViewerScreen(onBackClick: () -> Unit) {
    composable(AR_VIEWER_ROUTE) { backStackEntry ->
        val componentId = backStackEntry.arguments?.getString("componentId") ?: ""
        val title = backStackEntry.arguments?.getString("componentTitle") ?: ""
        val modelFileName = backStackEntry.arguments?.getString("modelFileName") ?: ""
        ArViewerScreen(
            componentId = componentId,
            componentTitle = title,
            modelFileName = modelFileName,
            onBackClick = onBackClick
        )
    }
}

fun NavController.navigateToCategory(categoryId: String) {
    navigate("category/$categoryId")
}

fun NavController.navigateToComponent(componentId: String) {
    navigate("component/$componentId")
}

fun NavController.navigateToArViewer(componentId: String, componentTitle: String, modelFileName: String) {
    val encodedModel = java.net.URLEncoder.encode(modelFileName.ifBlank { "none" }, "UTF-8")
    navigate("ar_viewer/$componentId/$componentTitle/$encodedModel")
}
