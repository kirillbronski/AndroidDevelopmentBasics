package com.kbcoding.androiddevelopmentbasics.di

import com.kbcoding.androiddevelopmentbasics.app.model.accounts.AccountsSource
import com.kbcoding.androiddevelopmentbasics.app.model.boxes.BoxesSource
import com.kbcoding.androiddevelopmentbasics.sources.accounts.RetrofitAccountsSource
import com.kbcoding.androiddevelopmentbasics.sources.boxes.RetrofitBoxesSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SourcesModule {

    @Binds
    abstract fun bindAccountsSource(
        retrofitAccountsSource: RetrofitAccountsSource
    ): AccountsSource

    @Binds
    abstract fun bindBoxesSource(
        retrofitBoxesSource: RetrofitBoxesSource
    ): BoxesSource
}