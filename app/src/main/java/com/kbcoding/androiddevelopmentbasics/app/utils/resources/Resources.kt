package com.kbcoding.androiddevelopmentbasics.app.utils.resources

import androidx.annotation.StringRes

interface Resources {
    fun getString(@StringRes stringRes: Int): String
}