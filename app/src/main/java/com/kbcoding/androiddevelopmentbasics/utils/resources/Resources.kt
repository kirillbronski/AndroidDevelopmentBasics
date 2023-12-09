package com.kbcoding.androiddevelopmentbasics.utils.resources

import androidx.annotation.StringRes

interface Resources {
    fun getString(@StringRes stringRes: Int): String
}