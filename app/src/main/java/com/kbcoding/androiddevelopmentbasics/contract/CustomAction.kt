package com.kbcoding.androiddevelopmentbasics.contract

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

class CustomAction(
    @DrawableRes val iconRes: Int,
    @StringRes val textRes: Int,
    val onCustomAction: Runnable
)