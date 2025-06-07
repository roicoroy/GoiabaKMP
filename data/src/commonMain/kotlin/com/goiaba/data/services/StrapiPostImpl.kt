package com.goiaba.data.services

import com.goiaba.data.models.PostsResponse
import com.goiaba.data.services.domain.StrapiPostRepository
import com.goiaba.shared.util.RequestState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.delay

class StrapiPostImpl : StrapiPostRepository {
    
    private val apiService = StrapiApiService()
    
    override fun getPosts(): Flow<RequestState<PostsResponse>> = flow {
        emit(RequestState.Loading)
        
        try {
            // Add a small delay to show loading state
            delay(500)
            
            val result = apiService.getPosts()
            
            result.fold(
                onSuccess = { postsResponse ->
                    emit(RequestState.Success(postsResponse))
                },
                onFailure = { exception ->
                    emit(RequestState.Error(exception.message ?: "Unknown error occurred"))
                }
            )
        } catch (e: Exception) {
            emit(RequestState.Error("Failed to fetch posts: ${e.message}"))
        }
    }
    
    override fun getPostById(id: Int): Flow<RequestState<PostsResponse.Data>> = flow {
        emit(RequestState.Loading)
        
        try {
            delay(300)
            
            val result = apiService.getPostById(id)
            
            result.fold(
                onSuccess = { post ->
                    emit(RequestState.Success(post))
                },
                onFailure = { exception ->
                    emit(RequestState.Error(exception.message ?: "Unknown error occurred"))
                }
            )
        } catch (e: Exception) {
            emit(RequestState.Error("Failed to fetch post: ${e.message}"))
        }
    }
    
    override suspend fun createPost(title: String): Result<PostsResponse.Data> {
        return try {
            apiService.createPost(title)
        } catch (e: Exception) {
            Result.failure(Exception("Failed to create post: ${e.message}"))
        }
    }
    
    override suspend fun updatePost(id: Int, title: String): Result<PostsResponse.Data> {
        return try {
            apiService.updatePost(id, title)
        } catch (e: Exception) {
            Result.failure(Exception("Failed to update post: ${e.message}"))
        }
    }
    
    override suspend fun deletePost(id: Int): Result<Unit> {
        return try {
            apiService.deletePost(id)
        } catch (e: Exception) {
            Result.failure(Exception("Failed to delete post: ${e.message}"))
        }
    }
}