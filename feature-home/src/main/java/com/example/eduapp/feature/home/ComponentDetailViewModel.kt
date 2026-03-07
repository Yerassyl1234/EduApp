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

@HiltViewModel
class ComponentDetailViewModel @Inject constructor(
    private val categoriesRepository: CategoriesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val componentId: String = savedStateHandle.get<String>("componentId") ?: ""

    private val _component = MutableStateFlow<Component?>(null)
    val component: StateFlow<Component?> = _component

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadComponent()
    }

    private fun loadComponent() {
        viewModelScope.launch {
            _isLoading.value = true
            val firebaseComponent = categoriesRepository.getComponent(componentId).getOrNull()
            
            if (firebaseComponent != null) {
                _component.value = firebaseComponent
            } else {
                _component.value = ContentData.getComponentById(componentId)
            }
            _isLoading.value = false
        }
    }
}
