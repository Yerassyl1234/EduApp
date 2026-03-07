package com.example.eduapp.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eduapp.core.data.repository.AuthRepository
import com.example.eduapp.core.data.repository.CategoriesRepository
import com.example.eduapp.core.domain.model.Category
import com.example.eduapp.core.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.eduapp.core.domain.model.ContentData

data class HomeUiState(
    val isLoading: Boolean = true,
    val user: User? = null,
    val categories: List<Category> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val categoriesRepository: CategoriesRepository
) : ViewModel() {

    // FIXED_CATEGORIES moved to ContentData in core module

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        loadData()
    }

    private fun loadData(showLoading: Boolean = true) {
        viewModelScope.launch {
            if (showLoading) {
                _uiState.value = _uiState.value.copy(isLoading = true)
            }
            val user = authRepository.getCurrentUser()
            val firebaseCourses = categoriesRepository.getCategories().getOrNull()
                ?.filter { it.published } ?: emptyList()
            
            val firebaseMap = firebaseCourses.associateBy { it.id }
            val mergedFixed = ContentData.FIXED_CATEGORIES.map { fixed ->
                firebaseMap[fixed.id] ?: fixed
            }
            val fixedIds = ContentData.FIXED_CATEGORIES.map { it.id }.toSet()
            val newFirebaseCourses = firebaseCourses.filter { it.id !in fixedIds }
            
            val allCategories = mergedFixed + newFirebaseCourses

            _uiState.value = HomeUiState(
                isLoading = false,
                user = user,
                categories = allCategories
            )
        }
    }

    fun refresh() {
        loadData(showLoading = false)
    }
}


