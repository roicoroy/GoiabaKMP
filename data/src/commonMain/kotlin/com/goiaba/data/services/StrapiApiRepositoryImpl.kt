package com.goiaba.data.services

import com.goiaba.data.models.PostsResponse
import com.goiaba.data.services.domain.StrapiApiRepository
import com.goiaba.shared.util.RequestState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class StrapiApiRepositoryImpl(
    private val apiService: StrapiApiService
) : StrapiApiRepository {
    
    override fun getPosts(): Flow<RequestState<PostsResponse>> = flow {
        emit(RequestState.Loading)
        try {
            val result = apiService.getPosts()
            result.fold(
                onSuccess = { posts ->
                    emit(RequestState.Success(posts))
                },
                onFailure = { error ->
                    emit(RequestState.Error(error.message ?: "Unknown error occurred"))
                }
            )
        } catch (e: Exception) {
            emit(RequestState.Error(e.message ?: "Network error occurred"))
        }
    }
    
    override fun getPostById(id: Int): Flow<RequestState<PostsResponse.Data>> = flow {
        emit(RequestState.Loading)
        try {
            val result = apiService.getPostById(id)
            result.fold(
                onSuccess = { post ->
                    emit(RequestState.Success(post))
                },
                onFailure = { error ->
                    emit(RequestState.Error(error.message ?: "Unknown error occurred"))
                }
            )
        } catch (e: Exception) {
            emit(RequestState.Error(e.message ?: "Network error occurred"))
        }
    }
    
    override suspend fun createPost(title: String): Result<PostsResponse.Data> {
        return apiService.createPost(title)
    }
    
    override suspend fun updatePost(id: Int, title: String): Result<PostsResponse.Data> {
        return apiService.updatePost(id, title)
    }
    
    override suspend fun deletePost(id: Int): Result<Unit> {
        return apiService.deletePost(id)
    }
}