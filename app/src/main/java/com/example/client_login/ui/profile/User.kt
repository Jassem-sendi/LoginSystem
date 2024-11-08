package com.example.client_login.ui.profile

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val _id: String,
    val avatar: String,
    val country: String,
    val countryCode: String,
    val countryData: CountryData,
    val currentStreak: Int,
    val dayEndsAfter: Int,
    val firstName: String,
    val inviteLink: String,
    val keywordsCount: Int,
    val lastName: String,
    val learningDuration: Int,
    val maxStreak: Int,
    val phone: String,
    val verified: Boolean
)