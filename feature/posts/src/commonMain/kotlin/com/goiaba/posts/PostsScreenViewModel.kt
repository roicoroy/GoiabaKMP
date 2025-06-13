package com.goiaba.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goiaba.data.models.posts.PostsResponse
import com.goiaba.data.networking.ApiClient
import com.goiaba.data.services.posts.domain.PostRepository
import com.goiaba.shared.util.RequestState
import com.goiaba.shared.util.TokenManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PostsScreenViewModel(
    private val strapiApiRepository: PostRepository,
) : ViewModel() {

    private val _posts = MutableStateFlow<RequestState<PostsResponse>>(RequestState.Loading)
    val posts: StateFlow<RequestState<PostsResponse>> = _posts.asStateFlow()

    // Modal state
    private val _isModalVisible = MutableStateFlow(false)
    val isModalVisible: StateFlow<Boolean> = _isModalVisible.asStateFlow()

    private val _isModalLoading = MutableStateFlow(false)
    val isModalLoading: StateFlow<Boolean> = _isModalLoading.asStateFlow()

    private val _modalMessage = MutableStateFlow<String?>(null)
    val modalMessage: StateFlow<String?> = _modalMessage.asStateFlow()

    // Authentication state
    private val _isLoggedIn = MutableStateFlow(TokenManager.isLoggedIn())
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _userEmail = MutableStateFlow(TokenManager.getUserEmail())
    val userEmail: StateFlow<String?> = _userEmail.asStateFlow()

    init {
        loadPosts()
    }

    private fun loadPosts() {
        viewModelScope.launch {
            strapiApiRepository.getPosts().collect { result ->
                _posts.value = result
            }
        }
    }

    fun refreshPosts() {
        loadPosts()
    }

    fun createPost(title: String) {
        viewModelScope.launch {
            _isModalLoading.value = true
            _modalMessage.value = null

            try {
                strapiApiRepository.createPost(title).collect { result ->
                    when (result) {
                        is RequestState.Loading -> {
                            _isModalLoading.value = true
                        }

                        is RequestState.Success -> {
                            _isModalLoading.value = false
                            _modalMessage.value = "Post created successfully!"
                            // Refresh the posts list
                            refreshPosts()
                            // Hide modal after a short delay
                            delay(500)
                            hideModal()
                        }

                        is RequestState.Error -> {
                            _isModalLoading.value = false
                            _modalMessage.value = "Failed to create post: ${result.message}"
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

    // Modal functions
    fun showModal() {
        _isModalVisible.value = true
        _modalMessage.value = null
    }

    fun hideModal() {
        _isModalVisible.value = false
        _modalMessage.value = null
    }

    // Authentication functions
    fun logout() {
        TokenManager.clearToken()
        ApiClient.clearAuthToken()
        _isLoggedIn.value = false
        _userEmail.value = null
        // Optionally refresh posts to show public content only
        refreshPosts()
    }

    fun updateAuthState() {
        _isLoggedIn.value = TokenManager.isLoggedIn()
        _userEmail.value = TokenManager.getUserEmail()
    }
}
