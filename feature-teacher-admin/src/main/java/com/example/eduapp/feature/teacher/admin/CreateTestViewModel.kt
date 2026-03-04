package com.example.eduapp.feature.teacher.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eduapp.core.data.repository.AuthRepository
import com.example.eduapp.core.data.repository.TestsRepository
import com.example.eduapp.core.domain.model.Question
import com.example.eduapp.core.domain.model.Test
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class QuestionDraft(
    val text: String = "",
    val options: List<String> = listOf("", "", "", ""),
    val correctAnswerIndex: Int = 0
)

data class CreateTestUiState(
    val testTitle: String = "",
    val testDescription: String = "",
    val questions: List<QuestionDraft> = listOf(QuestionDraft()),
    val currentQuestionIndex: Int = 0,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class CreateTestViewModel @Inject constructor(
    private val testsRepository: TestsRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateTestUiState())
    val uiState: StateFlow<CreateTestUiState> = _uiState

    fun onTitleChange(title: String) {
        _uiState.value = _uiState.value.copy(testTitle = title, error = null)
    }

    fun onDescriptionChange(desc: String) {
        _uiState.value = _uiState.value.copy(testDescription = desc, error = null)
    }

    fun onQuestionTextChange(text: String) {
        updateCurrentQuestion { it.copy(text = text) }
    }

    fun onOptionChange(optionIndex: Int, text: String) {
        updateCurrentQuestion {
            val options = it.options.toMutableList()
            options[optionIndex] = text
            it.copy(options = options)
        }
    }

    fun onCorrectAnswerChange(index: Int) {
        updateCurrentQuestion { it.copy(correctAnswerIndex = index) }
    }

    private fun updateCurrentQuestion(update: (QuestionDraft) -> QuestionDraft) {
        val state = _uiState.value
        val questions = state.questions.toMutableList()
        questions[state.currentQuestionIndex] = update(questions[state.currentQuestionIndex])
        _uiState.value = state.copy(questions = questions, error = null)
    }

    fun addQuestion() {
        val state = _uiState.value
        val newQuestions = state.questions.toMutableList()
        newQuestions.add(QuestionDraft())
        _uiState.value = state.copy(
            questions = newQuestions,
            currentQuestionIndex = newQuestions.size - 1
        )
    }

    fun deleteQuestion(index: Int) {
        val state = _uiState.value
        if (state.questions.size <= 1) return
        val newQuestions = state.questions.toMutableList()
        newQuestions.removeAt(index)
        val newIndex = when {
            state.currentQuestionIndex >= newQuestions.size -> newQuestions.size - 1
            state.currentQuestionIndex > index -> state.currentQuestionIndex - 1
            else -> state.currentQuestionIndex
        }
        _uiState.value = state.copy(
            questions = newQuestions,
            currentQuestionIndex = newIndex.coerceAtLeast(0)
        )
    }

    fun selectQuestion(index: Int) {
        _uiState.value = _uiState.value.copy(currentQuestionIndex = index)
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

    fun saveTest() {
        val state = _uiState.value
        if (state.testTitle.isBlank()) {
            _uiState.value = state.copy(error = "Тест атауын енгізіңіз")
            return
        }
        val emptyQuestion = state.questions.indexOfFirst { it.text.isBlank() }
        if (emptyQuestion != -1) {
            _uiState.value = state.copy(
                error = "${emptyQuestion + 1}-сұрақ мәтіні бос",
                currentQuestionIndex = emptyQuestion
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true, error = null)

            val user = authRepository.getCurrentUser()
            val test = Test(
                title = state.testTitle,
                description = state.testDescription,
                createdBy = user?.id ?: "",
                questionCount = state.questions.size
            )

            testsRepository.createTest(test).onSuccess { testId ->
                state.questions.forEach { draft ->
                    val question = Question(
                        testId = testId,
                        text = draft.text,
                        options = draft.options,
                        correctAnswerIndex = draft.correctAnswerIndex
                    )
                    testsRepository.addQuestion(question)
                }
                _uiState.value = _uiState.value.copy(isSaving = false, isSaved = true)
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(isSaving = false, error = e.message)
            }
        }
    }
}
