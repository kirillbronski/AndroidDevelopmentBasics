package com.kbcoding.androiddevelopmentbasics.apps.di

import com.kbcoding.androiddevelopmentbasics.model.CatsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import io.mockk.mockk
import javax.inject.Singleton

/**
 * This module replaces the real repository by a fake repository.
 * You need to uninstall the real module by using [UninstallModules]
 * annotation in all your test classes.
 */
@Module
@InstallIn(SingletonComponent::class)
class FakeRepositoriesModule {

    @Provides
    @Singleton
    fun provideCatsRepository(): CatsRepository {
        return mockk()
    }

}