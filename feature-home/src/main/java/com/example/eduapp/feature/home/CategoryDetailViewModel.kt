package com.example.eduapp.feature.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eduapp.core.data.repository.CategoriesRepository
import com.example.eduapp.core.domain.model.Component
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
        // Алдымен ContentData-дан тексеру (5 тұрақты секция)
        val localComponents = ContentData.getComponentsForCategory(categoryId)
        val fixedTitle = HomeViewModel.FIXED_CATEGORIES.find { it.id == categoryId }?.title

        if (localComponents.isNotEmpty()) {
            // Бұл 5 тұрақты секцияның бірі — ContentData қолдану
            _uiState.value = CategoryDetailUiState(
                isLoading = false,
                categoryTitle = fixedTitle ?: "",
                components = localComponents
            )
        } else {
            // Бұл мұғалім қосқан курс — Firebase-тен жүктеу
            viewModelScope.launch {
                _uiState.value = _uiState.value.copy(isLoading = true)
                val firebaseComponents = categoriesRepository.getComponents(categoryId).getOrNull() ?: emptyList()
                val categories = categoriesRepository.getCategories().getOrNull() ?: emptyList()
                val title = categories.find { it.id == categoryId }?.title ?: ""

                _uiState.value = CategoryDetailUiState(
                    isLoading = false,
                    categoryTitle = title,
                    components = firebaseComponents
                )
            }
        }
    }
}


