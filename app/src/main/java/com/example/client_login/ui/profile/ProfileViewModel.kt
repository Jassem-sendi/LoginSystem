package com.example.client_login.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.client_login.data.TokensDataStore
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ProfileViewModel (
    private val tokensDataStore: TokensDataStore,
    private val authClient: HttpClient
) :ViewModel(){
    private val _profileScreenState = MutableStateFlow(Profile())
    val profileScreenState: StateFlow<Profile> = _profileScreenState.asStateFlow()
    suspend fun getUserInfo(): UserInfo? {
        return try {
            val response = authClient.get("api/v2/user/me")
            response.body()
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
    fun Logout(){
        viewModelScope.launch {
            tokensDataStore.clearToken()
        }
    }
}
data class Profile(
    val userInfo: UserInfo? =null
)