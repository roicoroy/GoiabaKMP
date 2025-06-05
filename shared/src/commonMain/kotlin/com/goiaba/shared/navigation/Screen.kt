package com.goiaba.shared.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object HomeGraph : Screen()

    @Serializable
    data object About : Screen()

    @Serializable
    data class Details(
        val id: String
    ) : Screen()

    @Serializable
    data class PaymentCompleted(
        val isSuccess: Boolean? = null,
        val error: String? = null,
        val token: String? = null
    ) : Screen()
}