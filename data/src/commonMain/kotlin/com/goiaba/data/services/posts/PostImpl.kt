package com.goiaba.data.services.posts

import com.goiaba.data.models.posts.PostsResponse
import com.goiaba.data.models.posts.SinglePostResponse
import com.goiaba.data.services.posts.domain.PostRepository
import com.goiaba.shared.util.RequestState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow

class PostImpl : PostRepository {

    private val apiService = PostsService()

    override fun getPosts(): Flow<RequestState<PostsResponse>> = flow {
        emit(RequestState.Loading)

        try {
            // Add a small delay to show loading state
            delay(500)

            val result = apiService.getPosts()

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

    override fun getPostById(id: String): Flow<RequestState<SinglePostResponse>> = channelFlow {
        send(RequestState.Loading)

        try {
            delay(300)
            val result = apiService.getPostById(id.toInt())
            if (result.isSuccess()) {
                send(result)
            } else {
                send(RequestState.Error("exception ${result.getErrorMessage()}"))
            }
        } catch (e: Exception) {
            send(RequestState.Error("Failed to fetch post: ${e.message}"))
        }
    }

    override suspend fun createPost(title: String): Flow<RequestState<SinglePostResponse>> = flow {
        emit(RequestState.Loading)
        
        try {
            delay(300)
            val result = apiService.createPost(title)
            emit(result)
        } catch (e: Exception) {
            emit(RequestState.Error("Failed to create post: ${e.message}"))
        }
    }

    override suspend fun updatePost(
        id: Int,
        title: String
    ): Flow<RequestState<SinglePostResponse>> = flow {
        emit(RequestState.Loading)
        
        try {
            delay(300)
            val result = apiService.updatePost(id, title)
            emit(result)
        } catch (e: Exception) {
            emit(RequestState.Error("Failed to update post: ${e.message}"))
        }
    }

    override suspend fun deletePost(id: Int): Result<Unit> {
        return try {
            val result = apiService.deletePost(id)
            if (result.isSuccess()) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(result.getErrorMessage()))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Failed to delete post: ${e.message}"))
        }
    }
}