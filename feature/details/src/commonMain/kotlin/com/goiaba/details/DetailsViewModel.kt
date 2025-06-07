package com.goiaba.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goiaba.data.models.PostsResponse
import com.goiaba.data.services.domain.StrapiPostRepository
import com.goiaba.shared.util.RequestState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DetailsViewModel(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel(), KoinComponent {
    
    private val strapiPostRepository: StrapiPostRepository by inject()
    
    var id: String = savedStateHandle.get<String>("id") ?: ""
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    val post: StateFlow<RequestState<PostsResponse.Data>> = if (id.isNotEmpty()) {
        try {
            val postId = id.toIntOrNull()
            if (postId != null) {
                strapiPostRepository.getPostById(postId)
                    .stateIn(
                        scope = viewModelScope,
                        started = SharingStarted.WhileSubscribed(5000),
                        initialValue = RequestState.Loading
                    )
            } else {
                MutableStateFlow(RequestState.Error("Invalid post ID format")).asStateFlow()
            }
        } catch (e: Exception) {
            MutableStateFlow(RequestState.Error("Error loading post: ${e.message}")).asStateFlow()
        }
    } else {
        MutableStateFlow(RequestState.Error("No post ID provided")).asStateFlow()
    }
    
    fun clearError() {
        _errorMessage.value = null
    }
    
    fun retry() {
        // The StateFlow will automatically retry when collected again
        clearError()
    }
}