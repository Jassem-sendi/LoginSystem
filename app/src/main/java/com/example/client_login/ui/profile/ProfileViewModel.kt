package com.example.client_login.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.client_login.MainActivityContext
import com.example.client_login.Profile
import com.example.client_login.data.TokensDataStore
import com.example.client_login.ui.login.UiState
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class ProfileViewModel :ViewModel(){
    private val _profileScreenState = MutableStateFlow(Profile())
    val profileScreenState: StateFlow<com.example.client_login.ui.profile.Profile> = _profileScreenState.asStateFlow()
    private val tokenDataStore = TokensDataStore(MainActivityContext)

    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                ignoreUnknownKeys = true
            })
        }
    }
    suspend fun getUserInfo(): UserInfo? {
        return try {
            val token = tokenDataStore.getAccessToken()
            client.get("https://api.lissene.com/api/v2/user/me") {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
            }.body()
        } catch (e: Exception) {
            println("Error fetching user info: ${e.message}")
            null
        }
    }
    fun fetchUserInfo() {
        viewModelScope.launch {
            val userInfo = getUserInfo()
            _profileScreenState.value = _profileScreenState.value.copy(userInfo = userInfo)
        }
    }
}
data class Profile(
    val userInfo: UserInfo? =null
)