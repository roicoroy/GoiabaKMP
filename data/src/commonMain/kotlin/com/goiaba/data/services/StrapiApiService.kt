package com.goiaba.data.services

import com.goiaba.data.models.PostsResponse
import com.goiaba.data.networking.ApiClient
import com.goiaba.data.networking.posts
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*

@Serializable
data class SinglePostResponse(
    val data: PostsResponse.Data
)

@Serializable
data class CreatePostRequest(
    val data: CreatePostData
)

@Serializable
data class CreatePostData(
    val title: String
)

class StrapiApiService {
    
    suspend fun getPosts(): Result<PostsResponse> {
        return try {
            val response: HttpResponse = ApiClient.httpClient.get(posts)
            
            when (response.status) {
                HttpStatusCode.OK -> {
                    val postsResponse = response.body<PostsResponse>()
                    Result.success(postsResponse)
                }
                HttpStatusCode.Unauthorized -> {
                    Result.failure(Exception("Unauthorized: Invalid API token"))
                }
                HttpStatusCode.NotFound -> {
                    Result.failure(Exception("Posts endpoint not found"))
                }
                HttpStatusCode.InternalServerError -> {
                    Result.failure(Exception("Server error: Please try again later"))
                }
                else -> {
                    Result.failure(Exception("HTTP ${response.status.value}: ${response.status.description}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network error: ${e.message ?: "Unknown error occurred"}"))
        }
    }
    
    suspend fun getPostById(id: Int): Result<PostsResponse.Data> {
        return try {
            val response: HttpResponse = ApiClient.httpClient.get("$posts/$id")
            
            when (response.status) {
                HttpStatusCode.OK -> {
                    val singlePostResponse = response.body<SinglePostResponse>()
                    Result.success(singlePostResponse.data)
                }
                HttpStatusCode.NotFound -> {
                    Result.failure(Exception("Post with ID $id not found"))
                }
                HttpStatusCode.Unauthorized -> {
                    Result.failure(Exception("Unauthorized: Invalid API token"))
                }
                else -> {
                    Result.failure(Exception("HTTP ${response.status.value}: ${response.status.description}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network error: ${e.message ?: "Unknown error occurred"}"))
        }
    }
    
    suspend fun createPost(title: String): Result<PostsResponse.Data> {
        return try {
            val requestBody = CreatePostRequest(
                data = CreatePostData(title = title)
            )
            
            val response: HttpResponse = ApiClient.httpClient.post(posts) {
                setBody(requestBody)
            }
            
            when (response.status) {
                HttpStatusCode.OK, HttpStatusCode.Created -> {
                    val singlePostResponse = response.body<SinglePostResponse>()
                    Result.success(singlePostResponse.data)
                }
                HttpStatusCode.BadRequest -> {
                    Result.failure(Exception("Bad request: Invalid post data"))
                }
                HttpStatusCode.Unauthorized -> {
                    Result.failure(Exception("Unauthorized: Invalid API token"))
                }
                else -> {
                    Result.failure(Exception("HTTP ${response.status.value}: ${response.status.description}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network error: ${e.message ?: "Unknown error occurred"}"))
        }
    }
    
    suspend fun updatePost(id: Int, title: String): Result<PostsResponse.Data> {
        return try {
            val requestBody = CreatePostRequest(
                data = CreatePostData(title = title)
            )
            
            val response: HttpResponse = ApiClient.httpClient.put("$posts/$id") {
                setBody(requestBody)
            }
            
            when (response.status) {
                HttpStatusCode.OK -> {
                    val singlePostResponse = response.body<SinglePostResponse>()
                    Result.success(singlePostResponse.data)
                }
                HttpStatusCode.NotFound -> {
                    Result.failure(Exception("Post with ID $id not found"))
                }
                HttpStatusCode.Unauthorized -> {
                    Result.failure(Exception("Unauthorized: Invalid API token"))
                }
                else -> {
                    Result.failure(Exception("HTTP ${response.status.value}: ${response.status.description}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network error: ${e.message ?: "Unknown error occurred"}"))
        }
    }
    
    suspend fun deletePost(id: Int): Result<Unit> {
        return try {
            val response: HttpResponse = ApiClient.httpClient.delete("$posts/$id")
            
            when (response.status) {
                HttpStatusCode.OK, HttpStatusCode.NoContent -> {
                    Result.success(Unit)
                }
                HttpStatusCode.NotFound -> {
                    Result.failure(Exception("Post with ID $id not found"))
                }
                HttpStatusCode.Unauthorized -> {
                    Result.failure(Exception("Unauthorized: Invalid API token"))
                }
                else -> {
                    Result.failure(Exception("HTTP ${response.status.value}: ${response.status.description}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network error: ${e.message ?: "Unknown error occurred"}"))
        }
    }
}