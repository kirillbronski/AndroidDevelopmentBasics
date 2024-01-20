package com.kbcoding.androiddevelopmentbasics.app.di

import com.kbcoding.androiddevelopmentbasics.app.utils.async.DefaultLazyFlowFactory
import com.kbcoding.androiddevelopmentbasics.app.utils.async.DefaultLazyListenersFactory
import com.kbcoding.androiddevelopmentbasics.app.utils.async.LazyFlowFactory
import com.kbcoding.androiddevelopmentbasics.app.utils.async.LazyListenersFactory
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * This module provides factories for creating:
 * - [LazyListenersSubject]
 * - [LazyFlowSubject]
 */
@Module
@InstallIn(SingletonComponent::class)
interface LazyFlowsFactoryModule {
    @Binds
    fun bindLazyFlowFactory(
        factory: DefaultLazyFlowFactory
    ): LazyFlowFactory

    @Binds
    fun bindLazyListenersFactory(
        factory: DefaultLazyListenersFactory
    ): LazyListenersFactory

}