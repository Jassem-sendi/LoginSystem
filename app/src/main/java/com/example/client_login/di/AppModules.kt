package com.example.client_login.di

import com.example.client_login.data.TokensDataStore
import com.example.client_login.network.createAuthHttpClient
import com.example.client_login.network.createNoAuthHttpClient
import com.example.client_login.ui.login.LoginViewModel
import com.example.client_login.ui.profile.ProfileViewModel

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModules = module {
    single { TokensDataStore(get()) }
    single { createAuthHttpClient(get()) }
    single { createNoAuthHttpClient(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
}

