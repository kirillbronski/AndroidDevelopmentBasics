package com.kbcoding.androiddevelopmentbasics.model.accounts.entities

import com.kbcoding.androiddevelopmentbasics.model.boxes.entities.BoxAndSettings

/**
 * Account info with all boxes and their settings
 */
data class AccountFullData(
    val account: Account,
    val boxesAndSettings: List<BoxAndSettings>
)