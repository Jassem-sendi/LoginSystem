package com.example.client_login.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module



@Module
@ComponentScan("com.example.client_login")
class DataModule

@Module
@ComponentScan("com.example.client_login.ui")
class ViewModelModule

