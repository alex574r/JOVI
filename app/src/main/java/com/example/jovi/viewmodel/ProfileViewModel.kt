package com.example.jovi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.jovi.data.db.entity.PostEntity
import com.example.jovi.data.db.entity.UserEntity
import com.example.jovi.data.repository.PostRepository
import com.example.jovi.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
) : ViewModel() {

    private val _user = MutableStateFlow<UserEntity?>(null)
    val user: StateFlow<UserEntity?> = _user.asStateFlow()

    private val _posts = MutableStateFlow<List<PostEntity>>(emptyList())
    val posts: StateFlow<List<PostEntity>> = _posts.asStateFlow()

    private val _isEditing = MutableStateFlow(false)
    val isEditing: StateFlow<Boolean> = _isEditing.asStateFlow()

    fun loadUser(userId: Long = 1L) {
        viewModelScope.launch {
            userRepository.getUserById(userId).collect { _user.value = it }
        }
        viewModelScope.launch {
            postRepository.getPostsByAuthor(userId).collect { _posts.value = it }
        }
    }

    fun updateProfile(updated: UserEntity) {
        viewModelScope.launch {
            userRepository.update(updated)
            _user.value = updated
        }
    }

    class Factory(
        private val userRepository: UserRepository,
        private val postRepository: PostRepository,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(userRepository, postRepository) as T
        }
    }
}
