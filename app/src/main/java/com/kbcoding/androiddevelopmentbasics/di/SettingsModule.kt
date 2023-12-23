package com.kbcoding.androiddevelopmentbasics.di

import com.kbcoding.androiddevelopmentbasics.app.model.settings.AppSettings
import com.kbcoding.androiddevelopmentbasics.app.model.settings.SharedPreferencesAppSettings
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingsModule {

    @Binds
    @Singleton
    abstract fun bindAppSettings(
        appSettings: SharedPreferencesAppSettings
    ): AppSettings
}