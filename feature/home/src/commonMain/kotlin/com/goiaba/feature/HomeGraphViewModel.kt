package com.goiaba.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goiaba.data.services.domain.StrapiApiRepository
import com.goiaba.data.services.domain.StrapiAuthRepository
import com.goiaba.shared.util.RequestState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class HomeGraphViewModel(
    private val strapiAuthRep: StrapiAuthRepository,
    private val strapiApiRepository: StrapiApiRepository,
) : ViewModel() {

    var text: String = "This is home Fragment"

    val customer = strapiAuthRep.readCustomerFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RequestState.Loading
        )

    val posts = strapiApiRepository.getPosts()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RequestState.Loading
        )
}