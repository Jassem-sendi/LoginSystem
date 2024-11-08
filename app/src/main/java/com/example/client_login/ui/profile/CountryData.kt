package com.example.client_login.ui.profile

import kotlinx.serialization.Serializable

@Serializable
data class CountryData(
    val ISOCode: String,
    val __v: Int,
    val _id: String,
    val code: String,
    val createdAt: String,
    val first: String,
    val flag: String,
    val isEmailRegistrationAvailable: Boolean,
    val isPhoneRegistrationAvailable: Boolean,
    val name: String,
    val updatedAt: String
)