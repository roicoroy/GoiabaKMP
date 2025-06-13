package com.goiaba.data.services.profile.domain

import com.goiaba.data.models.profile.UsersMeResponse
import com.goiaba.shared.util.RequestState
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
//    http://localhost:1337/api/users/me?populate=*
    fun getUsersMe(): Flow<RequestState<UsersMeResponse>>
}