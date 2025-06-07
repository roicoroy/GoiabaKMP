package com.goiaba.data.services

import com.goiaba.data.models.PostsResponse
import com.goiaba.data.services.domain.StrapiPostRepository
import com.goiaba.shared.util.RequestState
import kotlinx.coroutines.flow.Flow

class StrapiPostImpl : StrapiPostRepository {
    override fun getPosts(): Flow<RequestState<PostsResponse>> {
        return TODO("Provide the return value")
    }
}