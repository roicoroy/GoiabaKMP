package com.goiaba.data.services.posts

import com.goiaba.data.models.posts.CreatePostRequest
import com.goiaba.data.models.posts.CreatePostData
import com.goiaba.data.models.posts.PostsResponse
import com.goiaba.data.models.posts.SinglePostResponse
import com.goiaba.data.networking.ApiClient
import com.goiaba.data.networking.populatePosts
import com.goiaba.data.networking.posts
import com.goiaba.shared.util.RequestState
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class PostsService {
    
    suspend fun getPosts(): RequestState<PostsResponse> {
        return try {
            val response: HttpResponse = ApiClient.httpClient.get(populatePosts)
            
            when (response.status) {
                HttpStatusCode.OK -> {
                    val postsResponse = response.body<PostsResponse>()
                    RequestState.Success(postsResponse)
                }
                HttpStatusCode.Unauthorized -> {
                    RequestState.Error(("Unauthorized: Invalid API token"))
                }
                HttpStatusCode.NotFound -> {
                    RequestState.Error(("Posts endpoint not found"))
                }
                HttpStatusCode.InternalServerError -> {
                    RequestState.Error(("Server error: Please try again later"))
                }
                else -> {
                    RequestState.Error(("HTTP ${response.status.value}: ${response.status.description}"))
                }
            }
        } catch (e: Exception) {
            RequestState.Error(("Network error: ${e.message ?: "Unknown error occurred"}"))
        }
    }
    
    suspend fun getPostById(id: Int): RequestState<SinglePostResponse> {
        return try {
            val response: HttpResponse = ApiClient.httpClient.get("${posts}${id}")
            
            when (response.status) {
                HttpStatusCode.OK -> {
                    val singlePostResponse = response.body<SinglePostResponse>()
                    RequestState.Success(singlePostResponse)
                }
                HttpStatusCode.NotFound -> {
                    RequestState.Error(("Post with ID $id not found"))
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

    suspend fun createPost(title: String): RequestState<SinglePostResponse> {
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
                    RequestState.Success(singlePostResponse)
                }
                HttpStatusCode.BadRequest -> {
                    RequestState.Error("Bad request: Invalid post data")
                }
                HttpStatusCode.Unauthorized -> {
                    RequestState.Error("Unauthorized: Invalid API token")
                }
                else -> {
                    RequestState.Error("HTTP ${response.status.value}: ${response.status.description}")
                }
            }
        } catch (e: Exception) {
            RequestState.Error("Network error: ${e.message ?: "Unknown error occurred"}")
        }
    }

    suspend fun updatePost(id: Int, title: String): RequestState<SinglePostResponse> {
        return try {
            val requestBody = CreatePostRequest(
                data = CreatePostData(title = title)
            )

            val response: HttpResponse = ApiClient.httpClient.put("${posts}${id}") {
                setBody(requestBody)
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    val singlePostResponse = response.body<SinglePostResponse>()
                    RequestState.Success(singlePostResponse)
                }
                HttpStatusCode.NotFound -> {
                    RequestState.Error("Post with ID $id not found")
                }
                HttpStatusCode.Unauthorized -> {
                    RequestState.Error("Unauthorized: Invalid API token")
                }
                else -> {
                    RequestState.Error("HTTP ${response.status.value}: ${response.status.description}")
                }
            }
        } catch (e: Exception) {
            RequestState.Error("Network error: ${e.message ?: "Unknown error occurred"}")
        }
    }

    suspend fun deletePost(id: Int): RequestState<Unit> {
        return try {
            val response: HttpResponse = ApiClient.httpClient.delete("${posts}${id}")

            when (response.status) {
                HttpStatusCode.OK, HttpStatusCode.NoContent -> {
                    RequestState.Success(Unit)
                }
                HttpStatusCode.NotFound -> {
                    RequestState.Error("Post with ID $id not found")
                }
                HttpStatusCode.Unauthorized -> {
                    RequestState.Error("Unauthorized: Invalid API token")
                }
                else -> {
                    RequestState.Error("HTTP ${response.status.value}: ${response.status.description}")
                }
            }
        } catch (e: Exception) {
            RequestState.Error("Network error: ${e.message ?: "Unknown error occurred"}")
        }
    }
}