package com.goiaba.data.services.posts.domain

import com.goiaba.data.models.posts.PostsResponse
import com.goiaba.data.models.posts.SinglePostResponse
import com.goiaba.shared.util.RequestState
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getPosts(): Flow<RequestState<PostsResponse>>
    fun getPostById(id: String): Flow<RequestState<SinglePostResponse>>
    suspend fun createPost(title: String): Flow<RequestState<SinglePostResponse>>
    suspend fun updatePost(id: Int, title: String): Flow<RequestState<SinglePostResponse>>
    suspend fun deletePost(id: Int): Result<Unit>
}