package com.example.eduapp.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eduapp.core.data.repository.AuthRepository
import com.example.eduapp.core.data.repository.CategoriesRepository
import com.example.eduapp.core.domain.model.Category
import com.example.eduapp.core.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = true,
    val user: User? = null,
    val categories: List<Category> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val categoriesRepository: CategoriesRepository
) : ViewModel() {

    companion object {
        val FIXED_CATEGORIES = listOf(
            Category(
                id = "cat_negizgi",
                title = "Компьютердің негізгі құрылғылары",
                imageUrl = "",
                order = 1
            ),
            Category(
                id = "cat_engizu",
                title = "Енгізу құрылғылары",
                imageUrl = "",
                order = 2
            ),
            Category(
                id = "cat_shygaru",
                title = "Шығару құрылғылары",
                imageUrl = "",
                order = 3
            ),
            Category(
                id = "cat_princip",
                title = "Компьютердің жұмыс істеу принципі",
                imageUrl = "",
                order = 4
            ),
            Category(
                id = "cat_architecture",
                title = "Қазіргі заманның архитектуралық шешімдері",
                imageUrl = "",
                order = 5
            )
        )
    }

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        loadData()
    }

    private fun loadData(showLoading: Boolean = true) {
        viewModelScope.launch {
            if (showLoading) {
                _uiState.value = _uiState.value.copy(isLoading = true)
            }
            val user = authRepository.getCurrentUser()

            // Firebase-тен мұғалім қосқан курстарды жүктеу (тек жарияланғандарын)
            val firebaseCourses = categoriesRepository.getCategories().getOrNull()
                ?.filter { it.published } ?: emptyList()

            // 5 тұрақты секция + жарияланған Firebase курстар (снизу)
            val allCategories = FIXED_CATEGORIES + firebaseCourses

            _uiState.value = HomeUiState(
                isLoading = false,
                user = user,
                categories = allCategories
            )
        }
    }

    fun refresh() {
        loadData(showLoading = false)
    }
}


