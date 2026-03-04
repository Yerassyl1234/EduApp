package com.example.eduapp.feature.test

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eduapp.core.data.repository.AuthRepository
import com.example.eduapp.core.data.repository.ResultsRepository
import com.example.eduapp.core.data.repository.TestsRepository
import com.example.eduapp.core.domain.model.Question
import com.example.eduapp.core.domain.model.TestResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TestUiState(
    val isLoading: Boolean = true,
    val testTitle: String = "",
    val questions: List<Question> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val selectedAnswers: MutableMap<Int, Int> = mutableMapOf(),
    val isFinished: Boolean = false,
    val score: Int = 0,
    val totalQuestions: Int = 0,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false
)

@HiltViewModel
class TestViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val testsRepository: TestsRepository,
    private val resultsRepository: ResultsRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val testId: String = savedStateHandle.get<String>("testId") ?: ""

    private val _uiState = MutableStateFlow(TestUiState())
    val uiState: StateFlow<TestUiState> = _uiState

    init {
        loadTest()
    }

    private fun loadTest() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            // Загружаем инфо о тесте (название)
            val tests = testsRepository.getTests().getOrNull() ?: emptyList()
            val testInfo = tests.find { it.id == testId }

            // Загружаем вопросы
            testsRepository.getQuestions(testId).onSuccess { questions ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    testTitle = testInfo?.title ?: "",
                    questions = questions
                )
            }.onFailure {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun selectAnswer(optionIndex: Int) {
        val currentIndex = _uiState.value.currentQuestionIndex
        val answers = _uiState.value.selectedAnswers.toMutableMap()
        answers[currentIndex] = optionIndex
        _uiState.value = _uiState.value.copy(selectedAnswers = answers)
    }

    fun nextQuestion() {
        val state = _uiState.value
        if (state.currentQuestionIndex < state.questions.size - 1) {
            _uiState.value = state.copy(currentQuestionIndex = state.currentQuestionIndex + 1)
        }
    }

    fun previousQuestion() {
        val state = _uiState.value
        if (state.currentQuestionIndex > 0) {
            _uiState.value = state.copy(currentQuestionIndex = state.currentQuestionIndex - 1)
        }
    }

    fun finishTest() {
        val state = _uiState.value
        var correctCount = 0
        state.questions.forEachIndexed { index, question ->
            val selected = state.selectedAnswers[index]
            if (selected == question.correctAnswerIndex) {
                correctCount++
            }
        }

        _uiState.value = state.copy(
            isFinished = true,
            score = correctCount,
            totalQuestions = state.questions.size,
            isSaving = true
        )

        viewModelScope.launch {
            val user = authRepository.getCurrentUser()
            val result = TestResult(
                userId = user?.id ?: "",
                userName = user?.name ?: "",
                testId = testId,
                testTitle = state.testTitle,
                score = correctCount,
                totalQuestions = state.questions.size
            )
            resultsRepository.saveResult(result)
            _uiState.value = _uiState.value.copy(isSaving = false, isSaved = true)
        }
    }
}
