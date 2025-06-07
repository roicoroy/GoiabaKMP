package com.goiaba.data.services.domain

import com.goiaba.shared.util.RequestState
import kotlinx.coroutines.flow.Flow

interface StrapiAuthRepository {
    fun getCurrentUserId(): String?
    fun readCustomerFlow(): Flow<RequestState<String>>
}