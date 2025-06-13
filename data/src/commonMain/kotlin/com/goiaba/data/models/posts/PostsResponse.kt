package com.goiaba.data.models.posts


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostsResponse(
    @SerialName("data")
    val data: List<PostsData>,
    @SerialName("meta")
    val meta: Meta
) {
    @Serializable
    data class PostsData(
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
    data class Meta(
        @SerialName("pagination")
        val pagination: Pagination
    ) {
        @Serializable
        data class Pagination(
            @SerialName("page")
            val page: Int,
            @SerialName("pageCount")
            val pageCount: Int,
            @SerialName("pageSize")
            val pageSize: Int,
            @SerialName("total")
            val total: Int
        )
    }
}