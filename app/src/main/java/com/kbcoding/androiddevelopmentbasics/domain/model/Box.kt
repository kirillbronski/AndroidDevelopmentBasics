package com.kbcoding.androiddevelopmentbasics.domain.model

import androidx.annotation.StringRes

data class Box(
    val id: Int,
    @StringRes val colorNameRes: Int,
    val colorValue: Int
)