package com.goiaba.data.models.posts


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SinglePostResponse(
    val data: PostResponse.PostData
)

@Serializable
data class CreatePostRequest(
    val data: CreatePostData
)

@Serializable
data class CreatePostData(
    val title: String
)

@Serializable
data class PostResponse(
    @SerialName("data")
    val data: PostData,
    @SerialName("meta")
    val meta: Meta
) {
    @Serializable
    data class PostData(
        @SerialName("attributes")
        val attributes: Attributes,
        @SerialName("id")
        val id: Int
    ) {
        @Serializable
        data class Attributes(
            @SerialName("createdAt")
            val createdAt: String,
            @SerialName("title")
            val title: String,
            @SerialName("updatedAt")
            val updatedAt: String
        )
    }

    @Serializable
    class Meta
}