package com.kbcoding.androiddevelopmentbasics.model.accounts.room.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.kbcoding.androiddevelopmentbasics.model.boxes.room.entities.AccountBoxSettingDbEntity
import com.kbcoding.androiddevelopmentbasics.model.boxes.room.entities.BoxDbEntity
import com.kbcoding.androiddevelopmentbasics.model.boxes.room.views.SettingDbView

/**
 * Fetch only ID and password from 'accounts' table.
 */
data class AccountSignInTuple(
    @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "password") val password: String
)

/**
 * Tuple for updating username by account id
 */
data class AccountUpdateUsernameTuple(
    @ColumnInfo(name = "id") @PrimaryKey val id: Long,
    @ColumnInfo(name = "username") val username: String
)

/**
 * Tuple used for querying account data + boxes with edited settings.
 */
data class AccountAndEditedBoxesTuple(
    @Embedded val accountDbEntity: AccountDbEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value =  AccountBoxSettingDbEntity::class,
            parentColumn = "account_id",
            entityColumn = "box_id"
        )
    )
    val boxes: List<BoxDbEntity>
)

/**
 * Tuple for querying account data + settings for all boxes + boxes data
 */
data class AccountAndAllSettingsTuple(
    @Embedded val accountDbEntity: AccountDbEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "account_id",
        entity = SettingDbView::class
    )
    val settings: List<SettingAndBoxTuple>
)

/**
 * Helper tuple class for [AccountAndAllSettingsTuple] with nested relation.
 */
data class SettingAndBoxTuple(
    @Embedded val accountBoxSettingsDbEntity: SettingDbView,
    @Relation(
        parentColumn = "box_id",
        entityColumn = "id"
    )
    val boxDbEntity: BoxDbEntity
)