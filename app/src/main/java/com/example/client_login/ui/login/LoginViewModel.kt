package com.example.client_login.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.client_login.MainActivityContext
import com.example.client_login.data.RefreshResponse
import com.example.client_login.data.TokensDataStore
import com.example.client_login.data.TokensInformation
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class LoginViewModel : ViewModel() {
    private val _loginScreenState = MutableStateFlow(UiState())
    val loginScreenState: StateFlow<UiState> = _loginScreenState.asStateFlow()
    fun updateUsername(username: String) {
        _loginScreenState.value = _loginScreenState.value.copy(phone = username)
    }

    fun updatePassword(password: String) {
        _loginScreenState.value = _loginScreenState.value.copy(password = password)
    }

    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                ignoreUnknownKeys = true
            })
        }
        install(Auth) {
            bearer {
                loadTokens {
                    BearerTokens(
                        accessToken = TokensDataStore(MainActivityContext).getAccessToken() ,
                        refreshToken = TokensDataStore(MainActivityContext).getRefreshToken() ,
                    )
                }
                refreshTokens {
                    val tokenDataStore = TokensDataStore(MainActivityContext)
                    val refreshToken = tokenDataStore.getRefreshToken()
                    val refreshResponse =
                        client.post("https://api.lissene.com/api/v2/auth/refresh") {
                            contentType(ContentType.Application.Json)
                            setBody(RefreshResponse(refreshToken))
                        }
                    if (refreshResponse.status.value == 200) {
                        val tokens: TokensInformation = refreshResponse.body()
                        TokensDataStore(MainActivityContext).saveToken(
                            accessToken = tokens.accessToken ,
                            refreshToken = tokens.refreshToken ,
                            isLoggedIn = true
                        )
                        BearerTokens(
                            accessToken = TokensDataStore(MainActivityContext).getAccessToken() ,
                            refreshToken = TokensDataStore(MainActivityContext).getRefreshToken() ,
                        )
                    } else {
                        TokensDataStore(MainActivityContext).saveToken("" , "" , false)
                        _loginScreenState.value = _loginScreenState.value.copy(loggedIn = false)
                        BearerTokens("" , "")
                    }

                }
            }
        }

    }
    fun login() {
        viewModelScope.launch {
            try {
                val response = client.post("https://api.lissene.com/api/v2/auth/login") {
                    contentType(ContentType.Application.Json)
                    setBody(
                        LoginInfo(
                            _loginScreenState.value.phone ,
                            _loginScreenState.value.password
                        )
                    )
                }
                if (response.status.value == 200) {
                    _loginScreenState.value = _loginScreenState.value.copy(
                        responseText = "You are logged In" ,
                        loggedIn = true
                    )
                    val tokens: TokensInformation = response.body()
                    TokensDataStore(MainActivityContext).saveToken(
                        accessToken = tokens.accessToken ,
                        refreshToken = tokens.refreshToken ,
                        isLoggedIn = true
                    )


                } else {
                    _loginScreenState.value =
                        _loginScreenState.value.copy(responseText = "Error ${response.bodyAsText()}")
                }

            } catch (e: Exception) {
                _loginScreenState.value =
                    _loginScreenState.value.copy(responseText = "Error ${e.message}")
            }
        }
    }

}

data class UiState(
    val phone: String = "55529601" ,
    val password: String = "123456789" ,
    val responseText: String = "" ,
    val loggedIn: Boolean = false
)

@Serializable
data class LoginInfo(
    val phone: String ,
    val password: String
)

