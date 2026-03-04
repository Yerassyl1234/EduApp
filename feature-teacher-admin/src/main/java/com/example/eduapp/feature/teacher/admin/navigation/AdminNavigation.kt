package com.example.eduapp.feature.teacher.admin.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.eduapp.feature.teacher.admin.AdminPanelScreen
import com.example.eduapp.feature.teacher.admin.CoursesScreen
import com.example.eduapp.feature.teacher.admin.CreateTestScreen
import com.example.eduapp.feature.teacher.admin.SectionEditScreen
import com.example.eduapp.feature.teacher.admin.StudentsListScreen

const val ADMIN_ROUTE = "admin"
const val CREATE_TEST_ROUTE = "create_test"
const val STUDENTS_LIST_ROUTE = "students_list"
const val COURSES_ROUTE = "courses"
const val SECTION_EDIT_ROUTE = "section_edit/{sectionId}"

fun NavGraphBuilder.adminPanelScreen(
    onCreateTestClick: () -> Unit,
    onTestListClick: () -> Unit,
    onStudentsClick: () -> Unit,
    onCoursesClick: () -> Unit
) {
    composable(ADMIN_ROUTE) {
        AdminPanelScreen(
            onCreateTestClick = onCreateTestClick,
            onTestListClick = onTestListClick,
            onStudentsClick = onStudentsClick,
            onCoursesClick = onCoursesClick
        )
    }
}

fun NavGraphBuilder.createTestScreen(onBackClick: () -> Unit) {
    composable(CREATE_TEST_ROUTE) {
        CreateTestScreen(onBackClick = onBackClick)
    }
}

fun NavGraphBuilder.studentsListScreen(onBackClick: () -> Unit) {
    composable(STUDENTS_LIST_ROUTE) {
        StudentsListScreen(onBackClick = onBackClick)
    }
}

fun NavGraphBuilder.coursesScreen(
    onBackClick: () -> Unit,
    onSectionClick: (String) -> Unit
) {
    composable(COURSES_ROUTE) {
        CoursesScreen(
            onBackClick = onBackClick,
            onSectionClick = onSectionClick
        )
    }
}

fun NavGraphBuilder.sectionEditScreen(onBackClick: () -> Unit) {
    composable(SECTION_EDIT_ROUTE) {
        SectionEditScreen(onBackClick = onBackClick)
    }
}

fun NavController.navigateToCreateTest() { navigate(CREATE_TEST_ROUTE) }
fun NavController.navigateToStudentsList() { navigate(STUDENTS_LIST_ROUTE) }
fun NavController.navigateToCourses() { navigate(COURSES_ROUTE) }
fun NavController.navigateToSectionEdit(sectionId: String) { navigate("section_edit/$sectionId") }

