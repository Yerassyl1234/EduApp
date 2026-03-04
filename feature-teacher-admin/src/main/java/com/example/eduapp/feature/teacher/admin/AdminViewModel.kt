package com.example.eduapp.feature.teacher.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eduapp.core.data.repository.ResultsRepository
import com.example.eduapp.core.data.repository.TestsRepository
import com.example.eduapp.core.domain.model.Test
import com.example.eduapp.core.domain.model.TestResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AdminUiState(
    val isLoading: Boolean = true,
    val tests: List<Test> = emptyList(),
    val allResults: List<TestResult> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val testsRepository: TestsRepository,
    private val resultsRepository: ResultsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState: StateFlow<AdminUiState> = _uiState

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val tests = testsRepository.getTests().getOrNull() ?: emptyList()
            val results = resultsRepository.getAllResults().getOrNull() ?: emptyList()
            _uiState.value = AdminUiState(
                isLoading = false,
                tests = tests,
                allResults = results
            )
        }
    }

    fun publishResult(resultId: String) {
        viewModelScope.launch {
            resultsRepository.publishResult(resultId)
            loadData() // Перезагрузить список
        }
    }

    fun deleteTest(testId: String) {
        viewModelScope.launch {
            testsRepository.deleteTest(testId)
            loadData()
        }
    }
}
