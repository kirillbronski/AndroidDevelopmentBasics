package com.kbcoding.androiddevelopmentbasics.core.navigation

import androidx.annotation.StringRes
import com.kbcoding.androiddevelopmentbasics.model.User

interface Navigator {

    fun showDetails(user: User)

    fun goBack()

    fun toast(@StringRes messageRes: Int)
}