package com.goiaba.data.services.domain

import com.goiaba.data.models.PostsResponse
import com.goiaba.shared.util.RequestState
import kotlinx.coroutines.flow.Flow

interface StrapiPostRepository {
    fun getPosts(): Flow<RequestState<PostsResponse>>
//    fun getPostById(id: Int): Flow<RequestState<PostsResponse.Data>>
//    suspend fun createPost(title: String): Result<PostsResponse.Data>
//    suspend fun updatePost(id: Int, title: String): Result<PostsResponse.Data>
//    suspend fun deletePost(id: Int): Result<Unit>
}