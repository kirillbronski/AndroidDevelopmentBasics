package com.kbcoding.androiddevelopmentbasics.app.di

import com.kbcoding.androiddevelopmentbasics.app.domain.accounts.AccountsSource
import com.kbcoding.androiddevelopmentbasics.app.domain.boxes.BoxesSource
import com.kbcoding.androiddevelopmentbasics.app.data.accounts.RetrofitAccountsSource
import com.kbcoding.androiddevelopmentbasics.app.data.boxes.RetrofitBoxesSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * This module binds concrete sources implementations to their
 * interfaces: [RetrofitAccountsSource] is bound to [AccountsSource]
 * and [RetrofitBoxesSource] is bound to [BoxesSource].
 */
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