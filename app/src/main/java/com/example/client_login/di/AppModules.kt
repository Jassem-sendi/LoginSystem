package com.example.client_login.di

import com.example.client_login.data.TokensDataStore
import com.example.client_login.network.createAuthHttpClient
import com.example.client_login.network.createNoAuthHttpClient
import com.example.client_login.ui.login.LoginViewModel
import com.example.client_login.ui.profile.ProfileViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModules = module {
    singleOf(::TokensDataStore)
    singleOf(::createNoAuthHttpClient)
    singleOf(::createAuthHttpClient)
    viewModelOf(::LoginViewModel)
    viewModelOf(::ProfileViewModel)
}

