package com.kbcoding.androiddevelopmentbasics.di

import androidx.paging.ExperimentalPagingApi
import com.kbcoding.androiddevelopmentbasics.data.DefaultLaunchesRepository
import com.kbcoding.androiddevelopmentbasics.domain.LaunchesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@ExperimentalPagingApi
@Module
@InstallIn(SingletonComponent::class)
interface RepositoriesModule {

    @Binds
    fun bindLaunchesRepository(
        launchesRepository: DefaultLaunchesRepository
    ): LaunchesRepository

}