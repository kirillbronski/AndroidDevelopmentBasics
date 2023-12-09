package com.kbcoding.androiddevelopmentbasics.model.boxes.room.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded

data class SettingsTuple(
    @ColumnInfo(name = "is_active") val isActive: Boolean
)

data class BoxAndSettingsTuple(
    @Embedded val boxDbEntity: BoxDbEntity,
    @Embedded val settingDbEntity: AccountBoxSettingDbEntity
)