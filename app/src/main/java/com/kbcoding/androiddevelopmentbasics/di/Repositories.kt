package com.kbcoding.androiddevelopmentbasics.di

import android.content.Context
import androidx.room.Room
import com.kbcoding.androiddevelopmentbasics.model.accounts.room.RoomAccountsRepository
import com.kbcoding.androiddevelopmentbasics.model.boxes.room.RoomBoxesRepository
import com.kbcoding.androiddevelopmentbasics.data.room.AppDatabase
import com.kbcoding.androiddevelopmentbasics.model.settings.AppSettings
import com.kbcoding.androiddevelopmentbasics.model.settings.SharedPreferencesAppSettings
import com.kbcoding.androiddevelopmentbasics.domain.repository.AccountsRepository
import com.kbcoding.androiddevelopmentbasics.domain.repository.BoxesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

object Repositories {

    private lateinit var applicationContext: Context

    // -- stuffs

    private val database: AppDatabase by lazy<AppDatabase> {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database.db")
            .createFromAsset("initial_database.db")
            .build()
    }

    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    private val appSettings: AppSettings by lazy {
        SharedPreferencesAppSettings(applicationContext)
    }

    // --- repositories

    val accountsRepository: AccountsRepository by lazy {
        RoomAccountsRepository(database.getAccountsDao(), appSettings, ioDispatcher)
    }

    val boxesRepository: BoxesRepository by lazy {
        RoomBoxesRepository(accountsRepository, database.getBoxesDao(), ioDispatcher)
    }

    /**
     * Call this method in all application components that may be created at app startup/restoring
     * (e.g. in onCreate of activities and services)
     */
    fun init(context: Context) {
        applicationContext = context
    }
}