package com.goiaba.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goiaba.data.models.profile.UsersMeResponse
import com.goiaba.data.services.profile.domain.ProfileRepository
import com.goiaba.shared.util.RequestState
import com.goiaba.shared.util.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class ProfileViewModel(
    private val apiRepository: ProfileRepository,
) : ViewModel(), KoinComponent {

    private val _isLoggedIn = MutableStateFlow(TokenManager.isLoggedIn())
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _userEmail = MutableStateFlow(TokenManager.getUserEmail())
    val userEmail: StateFlow<String?> = _userEmail.asStateFlow()

    private val _user = MutableStateFlow<RequestState<UsersMeResponse>>(RequestState.Loading)
    val user: StateFlow<RequestState<UsersMeResponse>> = _user.asStateFlow()

    init {
        getUSer()
    }

    private fun getUSer() {
        viewModelScope.launch {
            apiRepository.getUsersMe().collect { result ->
                _user.value = result
            }
        }
    }

    fun refreshPosts() {
        getUSer()
    }

}