package com.example.jovi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.jovi.data.db.entity.PostEntity
import com.example.jovi.data.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FeedViewModel(private val postRepository: PostRepository) : ViewModel() {
    private val _threads = MutableStateFlow<List<PostEntity>>(emptyList())
    val threads: StateFlow<List<PostEntity>> = _threads.asStateFlow()

    private val _reels = MutableStateFlow<List<PostEntity>>(emptyList())
    val reels: StateFlow<List<PostEntity>> = _reels.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadFeed()
    }

    private fun loadFeed() {
        viewModelScope.launch {
            postRepository.getThreads().collect { _threads.value = it }
        }
        viewModelScope.launch {
            postRepository.getReels().collect { _reels.value = it }
        }
    }

    fun toggleLike(post: PostEntity) {
        viewModelScope.launch {
            if (post.isLiked) postRepository.unlikePost(post.id)
            else postRepository.likePost(post.id)
        }
    }

    class Factory(private val repository: PostRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return FeedViewModel(repository) as T
        }
    }
}
