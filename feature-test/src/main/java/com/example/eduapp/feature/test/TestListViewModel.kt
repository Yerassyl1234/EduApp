package com.example.eduapp.feature.test

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eduapp.core.data.repository.TestsRepository
import com.example.eduapp.core.domain.model.Test
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TestListUiState(
    val isLoading: Boolean = true,
    val tests: List<Test> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class TestListViewModel @Inject constructor(
    private val testsRepository: TestsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TestListUiState())
    val uiState: StateFlow<TestListUiState> = _uiState

    init {
        loadTests()
    }

    private fun loadTests() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            testsRepository.getTests().onSuccess { tests ->
                _uiState.value = TestListUiState(isLoading = false, tests = tests)
            }.onFailure { e ->
                _uiState.value = TestListUiState(isLoading = false, error = e.message)
            }
        }
    }

    fun refresh() { loadTests() }
}
