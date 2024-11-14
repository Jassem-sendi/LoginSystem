package com.example.client_login.data
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
data class TokensDataStore(private val context: Context) {
    suspend fun getAccessToken(): String {
        return context.dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN] ?: ""
        }.firstOrNull() ?: ""
    }

    suspend fun saveToken(accessToken: String? , refreshToken: String?, isLoggedIn:Boolean?) {
        context.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = accessToken ?: ""
            preferences[REFRESH_TOKEN] = refreshToken ?: ""
            preferences[IS_LOGGED_IN] = isLoggedIn ?: false
        }
    }
    suspend fun getRefreshToken(): String {
        return context.dataStore.data.map { preferences ->
            preferences[REFRESH_TOKEN] ?: ""
        }.firstOrNull() ?: ""
    }
  suspend fun isLoggedIn(): Boolean {
      return context.dataStore.data.map { preferences ->
          preferences[IS_LOGGED_IN] ?: false
      }.firstOrNull() ?: false
    }
    suspend fun clearToken() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token_data")
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    }
}