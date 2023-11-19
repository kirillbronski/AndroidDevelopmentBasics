package com.kbcoding.androiddevelopmentbasics.di

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.kbcoding.androiddevelopmentbasics.data.repository.SQLiteAccountsRepositoryImpl
import com.kbcoding.androiddevelopmentbasics.data.repository.SQLiteBoxesRepositoryImpl
import com.kbcoding.androiddevelopmentbasics.data.settings.AppSettings
import com.kbcoding.androiddevelopmentbasics.data.settings.SharedPreferencesAppSettings
import com.kbcoding.androiddevelopmentbasics.data.sqlite.AppSQLiteHelper
import com.kbcoding.androiddevelopmentbasics.domain.repository.AccountsRepository
import com.kbcoding.androiddevelopmentbasics.domain.repository.BoxesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

object Repositories {

    private lateinit var applicationContext: Context

    // -- stuffs

    private val database: SQLiteDatabase by lazy<SQLiteDatabase> {
        AppSQLiteHelper(applicationContext).writableDatabase
    }

    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    private val appSettings: AppSettings by lazy {
        SharedPreferencesAppSettings(applicationContext)
    }

    // --- repositories

    val accountsRepository: AccountsRepository by lazy {
        SQLiteAccountsRepositoryImpl(database, appSettings, ioDispatcher)
    }

    val boxesRepository: BoxesRepository by lazy {
        SQLiteBoxesRepositoryImpl(database, accountsRepository, ioDispatcher)
    }

    /**
     * Call this method in all application components that may be created at app startup/restoring
     * (e.g. in onCreate of activities and services)
     */
    fun init(context: Context) {
        applicationContext = context
    }
}