package com.example.client_login.network

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
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun createAuthHttpClient(tokensDataStore: TokensDataStore): HttpClient {
    return HttpClient(Android) {
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
                        createNoAuthHttpClient(tokensDataStore).post("https://api.lissene.com/api/v2/auth/refresh") {
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
                        println("From refresh ${tokens.accessToken}")
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

}
fun createNoAuthHttpClient(): HttpClient {
    val noAuthClient = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                ignoreUnknownKeys = true
            })
        }
    }
    return noAuthClient
}