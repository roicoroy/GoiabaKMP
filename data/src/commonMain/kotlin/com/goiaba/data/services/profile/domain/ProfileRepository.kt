package com.goiaba.data.services.profile.domain

import com.goiaba.data.models.profile.AddressUpdateRequest
import com.goiaba.data.models.profile.AddressUpdateResponse
import com.goiaba.data.models.profile.UsersMeResponse
import com.goiaba.shared.util.RequestState
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getUsersMe(): Flow<RequestState<UsersMeResponse>>
    suspend fun updateAddress(addressId: String, request: AddressUpdateRequest): Flow<RequestState<AddressUpdateResponse>>
    suspend fun deleteAddress(addressId: String): Flow<RequestState<Boolean>>
}