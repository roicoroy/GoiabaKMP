package com.goiaba.data.services

import com.goiaba.data.services.domain.StrapiAuthRepository
import com.goiaba.shared.util.RequestState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow

class StrapiAuthImpl : StrapiAuthRepository {
    override fun getCurrentUserId(): String? {
        return "1234566"
    }
    override fun readCustomerFlow(): Flow<RequestState<String>> = channelFlow {
        try {
            val userId = getCurrentUserId()
            if (userId != null) {
                send(RequestState.Success(data = userId))
            } else {
                send(RequestState.Error("User is not available."))
            }
        } catch (e: Exception) {
            send(RequestState.Error("Error while reading a Customer information: ${e.message}"))
        }
    }
}