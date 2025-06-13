package com.goiaba.data.services.profile

import com.goiaba.data.models.profile.UsersMeResponse
import com.goiaba.data.networking.ApiClient
import com.goiaba.data.networking.apiUsersMe
import com.goiaba.shared.util.RequestState
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class ProfileService {
    suspend fun getUsersMe():RequestState<UsersMeResponse> {
        return try {
            val response: HttpResponse = ApiClient.httpClient.get(apiUsersMe)
            when (response.status) {
                HttpStatusCode.OK -> {
                    val usersMeResponseHttp = response.body<UsersMeResponse>()
                    RequestState.Success(usersMeResponseHttp)
                }
                HttpStatusCode.NotFound -> {
                    RequestState.Error(("User not found"))
                }
                HttpStatusCode.Unauthorized -> {
                    RequestState.Error(("Unauthorized: Invalid API token"))
                }
                else -> {
                    RequestState.Error(("HTTP ${response.status.value}: ${response.status.description}"))
                }
            }
        } catch (e: Exception) {
            RequestState.Error(("Network error: ${e.message ?: "Unknown error occurred"}"))
        }
    }
}