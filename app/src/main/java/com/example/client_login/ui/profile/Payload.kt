package com.example.client_login.ui.profile

import kotlinx.serialization.Serializable

@Serializable
data class Payload(
    val user: User
)