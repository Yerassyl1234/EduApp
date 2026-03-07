package com.example.eduapp.feature.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eduapp.core.data.repository.CategoriesRepository
import com.example.eduapp.core.domain.model.Component
import com.example.eduapp.core.domain.model.ContentData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CategoryDetailUiState(
    val isLoading: Boolean = true,
    val categoryTitle: String = "",
    val components: List<Component> = emptyList()
)

@HiltViewModel
class CategoryDetailViewModel @Inject constructor(
    private val categoriesRepository: CategoriesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val categoryId: String = savedStateHandle.get<String>("categoryId") ?: ""

    private val _uiState = MutableStateFlow(CategoryDetailUiState())
    val uiState: StateFlow<CategoryDetailUiState> = _uiState

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            val categories = categoriesRepository.getCategories().getOrNull() ?: emptyList()
            val firebaseCategory = categories.find { it.id == categoryId }
            val fixedTitle = ContentData.FIXED_CATEGORIES.find { it.id == categoryId }?.title
            val categoryTitle = firebaseCategory?.title ?: fixedTitle ?: ""

            val firebaseComponents = categoriesRepository.getComponents(categoryId).getOrNull() ?: emptyList()
            val localComponents = ContentData.getComponentsForCategory(categoryId)

            val firebaseMap = firebaseComponents.associateBy { it.id }
            val mergedFixed = localComponents.map { fixed ->
                firebaseMap[fixed.id] ?: fixed
            }
            val localIds = localComponents.map { it.id }.toSet()
            val newFirebaseComponents = firebaseComponents.filter { it.id !in localIds }
            
            val components = mergedFixed + newFirebaseComponents

            _uiState.value = CategoryDetailUiState(
                isLoading = false,
                categoryTitle = categoryTitle,
                components = components
            )
        }
    }
}


