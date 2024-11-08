package com.example.client_login

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.client_login.data.RefreshResponse
import com.example.client_login.data.TokensDataStore
import com.example.client_login.data.TokensInformation
import com.example.client_login.ui.login.LoginScreen
import com.example.client_login.ui.profile.ProfileScreen
import com.example.client_login.ui.theme.Client_LoginTheme
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.utils.EmptyContent.contentType
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

lateinit var tokensDataStore: TokensDataStore

lateinit var noAuthClient: HttpClient

lateinit var authClient: HttpClient

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tokensDataStore = TokensDataStore(this)

        noAuthClient = HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    ignoreUnknownKeys = true
                })
            }
        }
        authClient = HttpClient(Android) {
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
                            accessToken = tokensDataStore.getAccessToken(),
                            refreshToken = tokensDataStore.getRefreshToken(),
                        )
                    }

                    refreshTokens {
                        val refreshToken = tokensDataStore.getRefreshToken()

                        val refreshResponse =
                            noAuthClient.post("https://api.lissene.com/api/v2/auth/refresh") {
                                contentType(ContentType.Application.Json)
                                setBody(RefreshResponse(refreshToken))
                            }

                        if (refreshResponse.status.isSuccess()) {
                            val tokens: TokensInformation = refreshResponse.body()
                            tokensDataStore.saveToken(
                                accessToken = tokens.accessToken ,
                                refreshToken = tokens.refreshToken ,
                                isLoggedIn = true
                            )
                            BearerTokens(
                                accessToken = tokensDataStore.getAccessToken() ,
                                refreshToken = tokensDataStore.getRefreshToken() ,
                            )
                        } else {
                            tokensDataStore.saveToken("" , "" , false)
                            null
                        }
                    }
                }
            }
        }

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            var isLoggedIn by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) {
                isLoggedIn = tokensDataStore.isLoggedIn()
            }
            println("loginfo $isLoggedIn")
            Client_LoginTheme {
                NavHost(
                    navController = navController ,
                    startDestination = if (!isLoggedIn) Login else Profile
                ) {
                    composable<Login> {
                        LoginScreen(onNavigateToProfile = { navController.navigate(Profile) })
                    }
                    composable<Profile> {
                        ProfileScreen()
                    }
                }
            }
        }
    }
}

@Serializable
object Login

@Serializable
object Profile