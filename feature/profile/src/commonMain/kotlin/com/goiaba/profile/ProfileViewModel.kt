package com.goiaba.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goiaba.data.models.profile.UsersMeResponse
import com.goiaba.data.networking.ApiClient
import com.goiaba.data.services.profile.domain.ProfileRepository
import com.goiaba.shared.util.RequestState
import com.goiaba.shared.util.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ProfileViewModel : ViewModel(), KoinComponent {

    private val profileRepository: ProfileRepository by inject()

    private val _isLoggedIn = MutableStateFlow(TokenManager.isLoggedIn())
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _userEmail = MutableStateFlow(TokenManager.getUserEmail())
    val userEmail: StateFlow<String?> = _userEmail.asStateFlow()

    private val _user = MutableStateFlow<RequestState<UsersMeResponse>>(RequestState.Loading)
    val user: StateFlow<RequestState<UsersMeResponse>> = _user.asStateFlow()

    init {
        updateAuthState()
        if (_isLoggedIn.value) {
            loadUserProfile()
        }
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            _user.value = RequestState.Loading
            
            try {
                profileRepository.getUsersMe().collect { result ->
                    _user.value = result
                }
            } catch (e: Exception) {
                _user.value = RequestState.Error("Failed to load profile: ${e.message}")
            }
        }
    }

    fun refreshProfile() {
        if (_isLoggedIn.value) {
            loadUserProfile()
        }
    }

    fun updateAuthState() {
        _isLoggedIn.value = TokenManager.isLoggedIn()
        _userEmail.value = TokenManager.getUserEmail()
        
        // Load profile if user is logged in
        if (_isLoggedIn.value && _user.value is RequestState.Idle) {
            loadUserProfile()
        }
    }

    fun logout() {
        TokenManager.clearToken()
        ApiClient.clearAuthToken()
        _isLoggedIn.value = false
        _userEmail.value = null
        _user.value = RequestState.Idle
    }
}