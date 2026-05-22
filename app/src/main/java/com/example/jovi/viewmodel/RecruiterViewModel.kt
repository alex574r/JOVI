package com.example.jovi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.jovi.data.db.entity.UserEntity
import com.example.jovi.data.repository.PostRepository
import com.example.jovi.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RecruiterStats(
    val totalApplicants: Int = 0,
    val totalMatches: Int = 0,
    val activeVacancies: Int = 0,
    val profileViews: Int = 0,
    val weeklyApplications: List<Int> = listOf(4, 7, 3, 9, 12, 5, 8),
)

class RecruiterViewModel(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
) : ViewModel() {

    private val _candidates = MutableStateFlow<List<UserEntity>>(emptyList())
    val candidates: StateFlow<List<UserEntity>> = _candidates.asStateFlow()

    private val _stats = MutableStateFlow(
        RecruiterStats(
            totalApplicants = 48,
            totalMatches = 12,
            activeVacancies = 3,
            profileViews = 234,
        )
    )
    val stats: StateFlow<RecruiterStats> = _stats.asStateFlow()

    init {
        viewModelScope.launch {
            userRepository.getStudents().collect { _candidates.value = it }
        }
    }

    class Factory(
        private val userRepository: UserRepository,
        private val postRepository: PostRepository,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return RecruiterViewModel(userRepository, postRepository) as T
        }
    }
}
