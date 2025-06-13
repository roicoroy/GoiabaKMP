package com.goiaba.data.services.profile

import com.goiaba.data.models.profile.UsersMeResponse
import com.goiaba.data.services.profile.domain.ProfileRepository
import com.goiaba.shared.util.RequestState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProfileImpl : ProfileRepository {
    private val apiService = ProfileService()
    override fun getUsersMe(): Flow<RequestState<UsersMeResponse>> = flow {
        emit(RequestState.Loading)

        try {
            // Add a small delay to show loading state
            delay(500)

            val result = apiService.getUsersMe()

            if (result.isSuccess()) {
                emit(
                    RequestState.Success(result.getSuccessData())
                )
            } else {
                emit(RequestState.Error("Unknown error occurred"))
            }
        } catch (e: Exception) {
            emit(RequestState.Error("Failed to fetch posts: ${e.message}"))
        }
    }
}