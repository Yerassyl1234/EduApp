package com.example.eduapp.feature.teacher.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eduapp.core.data.repository.CategoriesRepository
import com.example.eduapp.core.domain.model.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.eduapp.core.domain.model.ContentData

data class CoursesUiState(
    val isLoading: Boolean = true,
    val categories: List<Category> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class CoursesViewModel @Inject constructor(
    private val categoriesRepository: CategoriesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CoursesUiState())
    val uiState: StateFlow<CoursesUiState> = _uiState

    init {
        loadCategories()
    }

    fun loadCategories() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result = categoriesRepository.getCategories()
            
            val firebaseCourses = result.getOrNull() ?: emptyList()
            val firebaseMap = firebaseCourses.associateBy { it.id }
            val mergedFixed = ContentData.FIXED_CATEGORIES.map { fixed ->
                firebaseMap[fixed.id] ?: fixed
            }
            val fixedIds = ContentData.FIXED_CATEGORIES.map { it.id }.toSet()
            val newFirebaseCourses = firebaseCourses.filter { it.id !in fixedIds }

            _uiState.value = CoursesUiState(
                isLoading = false,
                categories = mergedFixed + newFirebaseCourses,
                error = result.exceptionOrNull()?.message
            )
        }
    }

    fun addCategory(title: String) {
        viewModelScope.launch {
            val maxOrder = _uiState.value.categories.maxOfOrNull { it.order } ?: 0
            val tempCategory = Category(
                id = "temp_${System.currentTimeMillis()}",
                title = title,
                order = maxOrder + 1
            )
            _uiState.value = _uiState.value.copy(
                categories = _uiState.value.categories + tempCategory
            )
            categoriesRepository.addCategory(tempCategory)
            loadCategories()
        }
    }

    fun deleteCategory(categoryId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                categories = _uiState.value.categories.filter { it.id != categoryId }
            )
            categoriesRepository.deleteCategory(categoryId)
            loadCategories()
        }
    }

    fun updateCategoryTitle(categoryId: String, newTitle: String) {
        viewModelScope.launch {
            val category = _uiState.value.categories.find { it.id == categoryId } ?: return@launch
            _uiState.value = _uiState.value.copy(
                categories = _uiState.value.categories.map {
                    if (it.id == categoryId) it.copy(title = newTitle) else it
                }
            )
            categoriesRepository.updateCategory(category.copy(title = newTitle))
            loadCategories()
        }
    }

    fun publishCategory(categoryId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                categories = _uiState.value.categories.map {
                    if (it.id == categoryId) it.copy(published = true) else it
                }
            )
            categoriesRepository.publishCategory(categoryId)
            loadCategories()
        }
    }
}
