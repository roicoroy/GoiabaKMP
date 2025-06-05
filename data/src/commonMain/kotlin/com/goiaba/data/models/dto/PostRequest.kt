package com.goiaba.data.models.dto

import kotlinx.serialization.Serializable

@Serializable
data class PostRequest(
    val body: String,
    val title: String,
    val userId: Int
)
