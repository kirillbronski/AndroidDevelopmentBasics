package com.kbcoding.androiddevelopmentbasics.di

import com.kbcoding.androiddevelopmentbasics.domain.repository.AccountsRepository
import com.kbcoding.androiddevelopmentbasics.data.repository.AccountsRepositoryImpl
import com.kbcoding.androiddevelopmentbasics.domain.repository.BoxesRepository
import com.kbcoding.androiddevelopmentbasics.data.repository.BoxesRepositoryImpl

object Repositories {

    val accountsRepository: AccountsRepository = AccountsRepositoryImpl()

    val boxesRepository: BoxesRepository = BoxesRepositoryImpl(accountsRepository)

}