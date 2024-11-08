package com.example.client_login.ui.profile

import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    val code: Int,
    val payload: Payload,
    val status: String
)