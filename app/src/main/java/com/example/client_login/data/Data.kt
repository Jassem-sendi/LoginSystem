package com.example.client_login.data

import kotlinx.serialization.Serializable

@Serializable
class TokensInformation(
    val accessToken: String,
    val refreshToken: String,
)
@Serializable
data class RefreshResponse(
    val refreshToken: String
)