package com.kbcoding.androiddevelopmentbasics.di

import com.kbcoding.androiddevelopmentbasics.model.CatsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoriesModule {

    @Provides
    @Singleton
    fun provideCatsRepository(): CatsRepository = CatsRepository()

}