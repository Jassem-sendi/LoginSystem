package com.example.client_login.di

import android.system.Os.bind
import com.example.client_login.data.TokensDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModuels = module {
    single { TokensDataStore(get()) }
}