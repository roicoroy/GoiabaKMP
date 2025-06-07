package com.goiaba.data.services

import com.goiaba.data.models.PostsResponse
import com.goiaba.data.networking.ApiClient
import com.goiaba.data.networking.posts
import com.goiaba.data.util.NetworkError
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.delay

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
                    // Strapi returns single item in different format
                    val postData = response.body<Map<String, Any>>()
                    val data = postData["data"] as? Map<String, Any>
                    
                    if (data != null) {
                        val attributes = data["attributes"] as? Map<String, Any>
                        val postId = (data["id"] as? Number)?.toInt() ?: 0
                        
                        if (attributes != null) {
                            val post = PostsResponse.Data(
                                id = postId,
                                attributes = PostsResponse.Data.Attributes(
                                    title = attributes["title"] as? String ?: "",
                                    createdAt = attributes["createdAt"] as? String ?: "",
                                    updatedAt = attributes["updatedAt"] as? String ?: ""
                                )
                            )
                            Result.success(post)
                        } else {
                            Result.failure(Exception("Invalid post data structure"))
                        }
                    } else {
                        Result.failure(Exception("Post not found"))
                    }
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
            val requestBody = mapOf(
                "data" to mapOf(
                    "title" to title
                )
            )
            
            val response: HttpResponse = ApiClient.httpClient.post(posts) {
                setBody(requestBody)
            }
            
            when (response.status) {
                HttpStatusCode.OK, HttpStatusCode.Created -> {
                    val responseData = response.body<Map<String, Any>>()
                    val data = responseData["data"] as? Map<String, Any>
                    
                    if (data != null) {
                        val attributes = data["attributes"] as? Map<String, Any>
                        val postId = (data["id"] as? Number)?.toInt() ?: 0
                        
                        if (attributes != null) {
                            val post = PostsResponse.Data(
                                id = postId,
                                attributes = PostsResponse.Data.Attributes(
                                    title = attributes["title"] as? String ?: "",
                                    createdAt = attributes["createdAt"] as? String ?: "",
                                    updatedAt = attributes["updatedAt"] as? String ?: ""
                                )
                            )
                            Result.success(post)
                        } else {
                            Result.failure(Exception("Invalid response data structure"))
                        }
                    } else {
                        Result.failure(Exception("Failed to create post"))
                    }
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
            val requestBody = mapOf(
                "data" to mapOf(
                    "title" to title
                )
            )
            
            val response: HttpResponse = ApiClient.httpClient.put("$posts/$id") {
                setBody(requestBody)
            }
            
            when (response.status) {
                HttpStatusCode.OK -> {
                    val responseData = response.body<Map<String, Any>>()
                    val data = responseData["data"] as? Map<String, Any>
                    
                    if (data != null) {
                        val attributes = data["attributes"] as? Map<String, Any>
                        val postId = (data["id"] as? Number)?.toInt() ?: 0
                        
                        if (attributes != null) {
                            val post = PostsResponse.Data(
                                id = postId,
                                attributes = PostsResponse.Data.Attributes(
                                    title = attributes["title"] as? String ?: "",
                                    createdAt = attributes["createdAt"] as? String ?: "",
                                    updatedAt = attributes["updatedAt"] as? String ?: ""
                                )
                            )
                            Result.success(post)
                        } else {
                            Result.failure(Exception("Invalid response data structure"))
                        }
                    } else {
                        Result.failure(Exception("Failed to update post"))
                    }
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