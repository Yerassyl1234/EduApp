package com.example.eduapp.feature.test.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.eduapp.feature.test.TestListScreen
import com.example.eduapp.feature.test.TestResultScreen
import com.example.eduapp.feature.test.TestScreen

const val TEST_LIST_ROUTE = "test_list"
const val TEST_ROUTE = "test/{testId}"
const val TEST_RESULT_ROUTE = "test_result/{score}/{total}"

fun NavGraphBuilder.testListScreen(onTestClick: (String) -> Unit) {
    composable(TEST_LIST_ROUTE) {
        TestListScreen(onTestClick = onTestClick)
    }
}

fun NavGraphBuilder.testScreen(
    onBackClick: () -> Unit,
    onFinish: () -> Unit
) {
    composable(TEST_ROUTE) {
        TestScreen(onBackClick = onBackClick, onFinish = onFinish)
    }
}

fun NavGraphBuilder.testResultScreen(onBackToTests: () -> Unit) {
    composable(TEST_RESULT_ROUTE) { backStackEntry ->
        val score = backStackEntry.arguments?.getString("score")?.toIntOrNull() ?: 0
        val total = backStackEntry.arguments?.getString("total")?.toIntOrNull() ?: 0
        TestResultScreen(
            score = score,
            total = total,
            onBackToTests = onBackToTests
        )
    }
}

fun NavController.navigateToTest(testId: String) {
    navigate("test/$testId")
}

fun NavController.navigateToTestResult(score: Int, total: Int) {
    navigate("test_result/$score/$total") {
        popUpTo(TEST_LIST_ROUTE)
    }
}
