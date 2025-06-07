package com.goiaba.data.services

import com.goiaba.data.models.PostsResponse
import com.goiaba.data.networking.ApiClient
import com.goiaba.data.networking.posts
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class StrapiApiService {
    private val client = ApiClient.httpClient
    
    suspend fun getPosts(): Result<PostsResponse> {
        return try {
            val response = client.get(posts)
            if (response.status == HttpStatusCode.OK) {
                val postsResponse = response.body<PostsResponse>()
                Result.success(postsResponse)
            } else {
                Result.failure(Exception("HTTP ${response.status.value}: ${response.status.description}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getPostById(id: Int): Result<PostsResponse.Data> {
        return try {
            val response = client.get("$posts/$id")
            if (response.status == HttpStatusCode.OK) {
                val postData = response.body<PostsResponse.Data>()
                Result.success(postData)
            } else {
                Result.failure(Exception("HTTP ${response.status.value}: ${response.status.description}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun createPost(title: String): Result<PostsResponse.Data> {
        return try {
            val requestBody = mapOf(
                "data" to mapOf(
                    "title" to title
                )
            )
            
            val response = client.post(posts) {
                setBody(requestBody)
            }
            
            if (response.status == HttpStatusCode.OK || response.status == HttpStatusCode.Created) {
                val postData = response.body<PostsResponse.Data>()
                Result.success(postData)
            } else {
                Result.failure(Exception("HTTP ${response.status.value}: ${response.status.description}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updatePost(id: Int, title: String): Result<PostsResponse.Data> {
        return try {
            val requestBody = mapOf(
                "data" to mapOf(
                    "title" to title
                )
            )
            
            val response = client.put("$posts/$id") {
                setBody(requestBody)
            }
            
            if (response.status == HttpStatusCode.OK) {
                val postData = response.body<PostsResponse.Data>()
                Result.success(postData)
            } else {
                Result.failure(Exception("HTTP ${response.status.value}: ${response.status.description}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deletePost(id: Int): Result<Unit> {
        return try {
            val response = client.delete("$posts/$id")
            if (response.status == HttpStatusCode.OK || response.status == HttpStatusCode.NoContent) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("HTTP ${response.status.value}: ${response.status.description}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}