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
import com.example.eduapp.core.domain.model.ContentData

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
            var category = categories.find { it.id == sectionId }
            if (category == null) {
                category = ContentData.FIXED_CATEGORIES.find { it.id == sectionId }
            }
            
            val firebaseComponents = categoriesRepository.getComponents(sectionId).getOrNull() ?: emptyList()
            val localComponents = ContentData.getComponentsForCategory(sectionId)
            
            val firebaseMap = firebaseComponents.associateBy { it.id }
            val mergedFixed = localComponents.map { fixed ->
                firebaseMap[fixed.id] ?: fixed
            }
            val localIds = localComponents.map { it.id }.toSet()
            val newFirebaseComponents = firebaseComponents.filter { it.id !in localIds }
            
            val components = mergedFixed + newFirebaseComponents

            _uiState.value = SectionEditUiState(
                isLoading = false,
                category = category,
                components = components
            )
        }
    }

    fun addComponent(title: String, imageUrl: String, description: String, composition: List<String>, function: String) {
        viewModelScope.launch {
            val component = Component(
                id = "temp_${System.currentTimeMillis()}",
                categoryId = sectionId,
                title = title,
                imageUrl = imageUrl,
                description = description,
                composition = composition,
                function = function
            )
            _uiState.value = _uiState.value.copy(
                components = _uiState.value.components + component
            )
            categoriesRepository.addComponent(component.copy(id = ""))
            loadSection()
        }
    }

    fun deleteComponent(componentId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                components = _uiState.value.components.filter { it.id != componentId }
            )
            categoriesRepository.deleteComponent(componentId)
            loadSection()
        }
    }

    fun updateComponent(component: Component) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                components = _uiState.value.components.map {
                    if (it.id == component.id) component else it
                }
            )
            categoriesRepository.updateComponent(component)
            loadSection()
        }
    }

    fun updateSectionTitle(newTitle: String) {
        viewModelScope.launch {
            val category = _uiState.value.category ?: return@launch
            _uiState.value = _uiState.value.copy(
                category = category.copy(title = newTitle)
            )
            categoriesRepository.updateCategory(category.copy(title = newTitle))
            loadSection()
        }
    }
}
