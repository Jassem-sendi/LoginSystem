package com.example.client_login

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.client_login.data.TokensDataStore
import com.example.client_login.ui.login.LoginScreen
import com.example.client_login.ui.login.LoginViewModel
import com.example.client_login.ui.profile.ProfileScreen
import com.example.client_login.ui.theme.Client_LoginTheme
import kotlinx.serialization.Serializable

@SuppressLint("StaticFieldLeak")
lateinit var MainActivityContext: Context

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainActivityContext = this
        val tokenDataStore = TokensDataStore(MainActivityContext)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            var isLoggedIn by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) {
                isLoggedIn = tokenDataStore.isLoggedIn()
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