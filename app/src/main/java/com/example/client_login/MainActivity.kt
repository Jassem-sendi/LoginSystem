package com.example.client_login

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
import com.example.client_login.data.TokensDataStore
import com.example.client_login.di.appModules
import com.example.client_login.ui.login.LoginScreen
import com.example.client_login.ui.profile.ProfileScreen
import com.example.client_login.ui.theme.Client_LoginTheme
import kotlinx.serialization.Serializable
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.mp.KoinPlatform.getKoin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startKoin {
            androidContext(applicationContext)
            androidLogger()
            modules(appModules)
        }
        val tokensDataStore = getKoin().get<TokensDataStore>()


        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            var isLoggedIn by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) {
                isLoggedIn = tokensDataStore.isLoggedIn()
            }
            Client_LoginTheme {
                NavHost(
                    navController = navController ,
                    startDestination = if (!isLoggedIn) Login else Profile
                ) {
                    composable<Login> {
                        LoginScreen(onNavigateToProfile = {
                            navController.navigate(Profile)

                        })
                    }
                    composable<Profile> {
                        ProfileScreen(logout = {
                            navController.navigate(Login)
                            navController.clearBackStack<Login>()

                        })
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