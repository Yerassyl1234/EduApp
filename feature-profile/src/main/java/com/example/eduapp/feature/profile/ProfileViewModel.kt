package com.example.eduapp.feature.profile

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eduapp.core.data.repository.AuthRepository
import com.example.eduapp.core.data.repository.ResultsRepository
import com.example.eduapp.core.domain.model.HelpRequest
import com.example.eduapp.core.domain.model.TestResult
import com.example.eduapp.core.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

data class ProfileUiState(
    val isLoading: Boolean = true,
    val user: User? = null,
    val results: List<TestResult> = emptyList(),
    val editName: String = "",
    val editEmail: String = "",
    val editPassword: String = "",
    val settingsMessage: String? = null,
    val isSavingSettings: Boolean = false,
    val helpText: String = "",
    val helpSent: Boolean = false,
    val isSendingHelp: Boolean = false,
    val photoUri: Uri? = null,
    val helpRequests: List<HelpRequest> = emptyList()
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val resultsRepository: ResultsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    init {
        loadProfile()
    }

    private fun loadProfile(showLoading: Boolean = true) {
        viewModelScope.launch {
            if (showLoading) {
                _uiState.value = _uiState.value.copy(isLoading = true)
            }
            val user = authRepository.getCurrentUser()
            val results = user?.let {
                if (it.role == "teacher") {
                    resultsRepository.getResultsForUser(it.id).getOrNull()
                } else {
                    resultsRepository.getPublishedResultsForUser(it.id).getOrNull()
                }
            } ?: emptyList()

            val photoUri = user?.photoUrl?.takeIf { it.isNotBlank() }?.let { Uri.parse(it) }

            val helpRequests = if (user?.role == "teacher") {
                authRepository.getHelpRequests().getOrNull() ?: emptyList()
            } else {
                emptyList()
            }

            _uiState.value = ProfileUiState(
                isLoading = false,
                user = user,
                results = results,
                editName = user?.name ?: "",
                editEmail = user?.email ?: "",
                photoUri = photoUri,
                helpRequests = helpRequests
            )
        }
    }

    fun updatePhoto(uri: Uri, context: Context) {
        viewModelScope.launch {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val file = File(context.filesDir, "avatar_${System.currentTimeMillis()}.jpg")
                inputStream?.use { input ->
                    file.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                val localUri = Uri.fromFile(file).toString()
                authRepository.updatePhotoUrl(localUri)
                _uiState.value = _uiState.value.copy(photoUri = Uri.parse(localUri))
            } catch (e: Exception) { }
        }
    }

    fun onEditNameChange(name: String) {
        _uiState.value = _uiState.value.copy(editName = name, settingsMessage = null)
    }

    fun onEditEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(editEmail = email, settingsMessage = null)
    }

    fun onEditPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(editPassword = password, settingsMessage = null)
    }

    fun saveSettings() {
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.value = state.copy(isSavingSettings = true, settingsMessage = null)
            try {
                if (state.editName != state.user?.name && state.editName.isNotBlank()) {
                    authRepository.updateUserName(state.editName)
                }
                if (state.editEmail != state.user?.email && state.editEmail.isNotBlank()) {
                    authRepository.updateEmail(state.editEmail)
                }
                if (state.editPassword.isNotBlank()) {
                    if (state.editPassword.length < 6) {
                        _uiState.value = _uiState.value.copy(
                            isSavingSettings = false,
                            settingsMessage = "Құпия сөз кемінде 6 таңба болуы керек"
                        )
                        return@launch
                    }
                    authRepository.updatePassword(state.editPassword)
                }
                val updatedUser = authRepository.getCurrentUser()
                _uiState.value = _uiState.value.copy(
                    isSavingSettings = false,
                    user = updatedUser,
                    editName = updatedUser?.name ?: "",
                    editEmail = updatedUser?.email ?: "",
                    editPassword = "",
                    settingsMessage = "Сәтті сақталды ✓"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSavingSettings = false,
                    settingsMessage = "Қате: ${e.message}"
                )
            }
        }
    }

    fun onHelpTextChange(text: String) {
        _uiState.value = _uiState.value.copy(helpText = text, helpSent = false)
    }

    fun sendHelpRequest() {
        val state = _uiState.value
        if (state.helpText.isBlank()) return
        viewModelScope.launch {
            _uiState.value = state.copy(isSendingHelp = true)
            try {
                authRepository.sendHelpRequest(
                    userId = state.user?.id ?: "",
                    userName = state.user?.name ?: "",
                    message = state.helpText
                )
                _uiState.value = _uiState.value.copy(
                    isSendingHelp = false,
                    helpText = "",
                    helpSent = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isSendingHelp = false)
            }
        }
    }

    fun signOut() {
        authRepository.signOut()
    }

    fun refresh() {
        loadProfile(showLoading = false)
    }
}
