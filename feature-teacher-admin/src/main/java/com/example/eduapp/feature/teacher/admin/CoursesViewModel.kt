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
            _uiState.value = CoursesUiState(
                isLoading = false,
                categories = result.getOrNull() ?: emptyList(),
                error = result.exceptionOrNull()?.message
            )
        }
    }

    fun addCategory(title: String) {
        viewModelScope.launch {
            val maxOrder = _uiState.value.categories.maxOfOrNull { it.order } ?: 0
            val category = Category(
                title = title,
                order = maxOrder + 1
            )
            categoriesRepository.addCategory(category)
            loadCategories()
        }
    }

    fun deleteCategory(categoryId: String) {
        viewModelScope.launch {
            categoriesRepository.deleteCategory(categoryId)
            loadCategories()
        }
    }

    fun updateCategoryTitle(categoryId: String, newTitle: String) {
        viewModelScope.launch {
            val category = _uiState.value.categories.find { it.id == categoryId } ?: return@launch
            categoriesRepository.updateCategory(category.copy(title = newTitle))
            loadCategories()
        }
    }
}
