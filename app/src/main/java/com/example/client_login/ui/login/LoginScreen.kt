package com.example.client_login.ui.login

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = koinViewModel() ,
    onNavigateToProfile: () -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val ui by loginViewModel.loginScreenState.collectAsState()
    println("Logged In:"+ui.loggedIn)
    Column(
        verticalArrangement = Arrangement.Center ,
        horizontalAlignment = Alignment.CenterHorizontally ,
        modifier = modifier
            .fillMaxSize()
    ) {
        TextField(
            value = ui.phone ,
            onValueChange = {
                loginViewModel.updateUsername(it)
            } ,
            label = { Text(text = "Phone Number") }
        )
        Spacer(modifier = Modifier.height(10.dp))
        TextField(
            value = ui.password ,
            onValueChange = {
                loginViewModel.updatePassword(it)
            } ,
            label = { Text(text = "Password") }
        )
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            if (ui.loggedIn){
                onNavigateToProfile()
            } else {
                loginViewModel.login()
            }
        }) {
            Text("Login")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(ui.responseText)
    }
}