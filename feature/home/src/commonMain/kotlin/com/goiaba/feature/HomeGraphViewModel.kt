package com.goiaba.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goiaba.data.services.domain.StrapiAuthRepository
import com.goiaba.shared.util.RequestState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import org.koin.compose.viewmodel.koinViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

class HomeGraphViewModel(
    private val strapiAuthRep: StrapiAuthRepository,
) : ViewModel() {

    var text: String = "This is home Fragment"
//    private var _searchText: MutableStateFlow<String> = MutableStateFlow("")
//    open var searchText = _searchText.asStateFlow()
val customer = strapiAuthRep.readCustomerFlow()
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RequestState.Loading
    )

    val products = customer.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RequestState.Loading
    )


}