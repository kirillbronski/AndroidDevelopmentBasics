package com.kbcoding.androiddevelopmentbasics.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kbcoding.androiddevelopmentbasics.model.boxes.room.entities.AccountBoxSettingDbEntity
import com.kbcoding.androiddevelopmentbasics.model.accounts.room.entities.AccountDbEntity
import com.kbcoding.androiddevelopmentbasics.model.boxes.room.entities.BoxDbEntity
import com.kbcoding.androiddevelopmentbasics.model.accounts.room.AccountsDao
import com.kbcoding.androiddevelopmentbasics.model.boxes.room.BoxesDao

@Database(
    version = 1,
    entities = [
        AccountDbEntity::class,
        BoxDbEntity::class,
        AccountBoxSettingDbEntity::class
    ]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getAccountsDao(): AccountsDao

    abstract fun getBoxesDao(): BoxesDao

}