package com.example.eduapp.feature.teacher.admin

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eduapp.core.data.repository.CategoriesRepository
import com.example.eduapp.core.domain.model.Category
import com.example.eduapp.core.domain.model.Component
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SectionEditUiState(
    val isLoading: Boolean = true,
    val category: Category? = null,
    val components: List<Component> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class SectionEditViewModel @Inject constructor(
    private val categoriesRepository: CategoriesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val sectionId: String = savedStateHandle["sectionId"] ?: ""

    private val _uiState = MutableStateFlow(SectionEditUiState())
    val uiState: StateFlow<SectionEditUiState> = _uiState

    init {
        loadSection()
    }

    fun loadSection() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val categories = categoriesRepository.getCategories().getOrNull() ?: emptyList()
            val category = categories.find { it.id == sectionId }
            val components = categoriesRepository.getComponents(sectionId).getOrNull() ?: emptyList()
            _uiState.value = SectionEditUiState(
                isLoading = false,
                category = category,
                components = components
            )
        }
    }

    fun addComponent(title: String, description: String, composition: List<String>, function: String) {
        viewModelScope.launch {
            val component = Component(
                categoryId = sectionId,
                title = title,
                description = description,
                composition = composition,
                function = function
            )
            categoriesRepository.addComponent(component)
            loadSection()
        }
    }

    fun deleteComponent(componentId: String) {
        viewModelScope.launch {
            categoriesRepository.deleteComponent(componentId)
            loadSection()
        }
    }

    fun updateComponent(component: Component) {
        viewModelScope.launch {
            categoriesRepository.updateComponent(component)
            loadSection()
        }
    }

    fun updateSectionTitle(newTitle: String) {
        viewModelScope.launch {
            val category = _uiState.value.category ?: return@launch
            categoriesRepository.updateCategory(category.copy(title = newTitle))
            loadSection()
        }
    }
}
