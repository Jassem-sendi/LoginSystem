package com.example.client_login.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.client_login.authClient
import com.example.client_login.data.TokensDataStore
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.mp.KoinPlatform.getKoin

class ProfileViewModel :ViewModel(){

    private val _profileScreenState = MutableStateFlow(Profile())
    val profileScreenState: StateFlow<Profile> = _profileScreenState.asStateFlow()
    val tokensDataStore = getKoin().get<TokensDataStore>()
    suspend fun getUserInfo(): UserInfo? {
        return try {
            val response = authClient.get("https://api.lissene.com/api/v2/user/me")

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