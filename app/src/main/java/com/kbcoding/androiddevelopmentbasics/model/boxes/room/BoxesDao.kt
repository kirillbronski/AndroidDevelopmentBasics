package com.kbcoding.androiddevelopmentbasics.model.boxes.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.kbcoding.androiddevelopmentbasics.model.boxes.room.entities.AccountBoxSettingDbEntity
import com.kbcoding.androiddevelopmentbasics.model.boxes.room.entities.BoxAndSettingsTuple
import com.kbcoding.androiddevelopmentbasics.model.boxes.room.entities.BoxDbEntity
import com.kbcoding.androiddevelopmentbasics.model.boxes.room.views.SettingWithEntitiesTuple
import kotlinx.coroutines.flow.Flow

@Dao
interface BoxesDao {

    @Transaction
    @Query("SELECT * FROM settings_view WHERE account_id = :accountId")
    fun getBoxesAndSettings(accountId: Long): Flow<List<SettingWithEntitiesTuple>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setActiveFlagForBox(accountBoxSetting: AccountBoxSettingDbEntity)

}