package com.goiaba.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goiaba.data.services.posts.domain.PostRepository
import com.goiaba.shared.util.RequestState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DetailsViewModel(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel(), KoinComponent {

    private val strapiPostRepository: PostRepository by inject()

    var id: String = savedStateHandle.get<String>("id") ?: ""

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Modal state
    private val _isModalVisible = MutableStateFlow(false)
    val isModalVisible: StateFlow<Boolean> = _isModalVisible.asStateFlow()

    private val _isModalLoading = MutableStateFlow(false)
    val isModalLoading: StateFlow<Boolean> = _isModalLoading.asStateFlow()

    private val _modalMessage = MutableStateFlow<String?>(null)
    val modalMessage: StateFlow<String?> = _modalMessage.asStateFlow()

    // Navigation state for post deletion
    private val _shouldNavigateToHome = MutableStateFlow(false)
    val shouldNavigateToHome: StateFlow<Boolean> = _shouldNavigateToHome.asStateFlow()

    var post = strapiPostRepository.getPostById(
        id
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RequestState.Loading
    )

    fun clearError() {
        _errorMessage.value = null
    }

    fun retry() {
        post = strapiPostRepository.getPostById(id).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RequestState.Loading
        )
        // The StateFlow will automatically retry when collected again
        clearError()
    }

    fun updatePost(title: String) {
        if (id.isEmpty()) return

        viewModelScope.launch {
            _isModalLoading.value = true
            _modalMessage.value = null

            try {
                strapiPostRepository.updatePost(id.toInt(), title).collect { result ->
                    when (result) {
                        is RequestState.Loading -> {
                            _isModalLoading.value = true
                        }

                        is RequestState.Success -> {
                            _isModalLoading.value = false
                            _modalMessage.value = "Post updated successfully!"
                            // Trigger refresh of the post data
                            retry()
                            // Hide modal after a short delay
                            delay(500)
                            hideModal()
                        }

                        is RequestState.Error -> {
                            _isModalLoading.value = false
                            _modalMessage.value = "Failed to update post: ${result.message}"
                        }

                        else -> {
                            _isModalLoading.value = false
                        }
                    }
                }
            } catch (e: Exception) {
                _isModalLoading.value = false
                _modalMessage.value = "Error: ${e.message}"
            }
        }
    }

    fun deletePost() {
        if (id.isEmpty()) return

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val result = strapiPostRepository.deletePost(id.toInt())
                _isLoading.value = false

                if (result.isSuccess) {
                    _modalMessage.value = "Post deleted successfully!"
                    // Wait a moment to show the success message, then navigate
                    delay(1500)
                    _shouldNavigateToHome.value = true
                } else {
                    _errorMessage.value =
                        "Failed to delete post: ${result.exceptionOrNull()?.message}"
                }
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = "Error deleting post: ${e.message}"
            }
        }
    }

    // Modal functions
    fun showModal() {
        _isModalVisible.value = true
        _modalMessage.value = null
    }

    fun hideModal() {
        _isModalVisible.value = false
        _modalMessage.value = null
    }

    // Navigation functions
    fun onNavigatedToHome() {
        _shouldNavigateToHome.value = false
    }
}