package com.kbcoding.androiddevelopmentbasics.core.navigator

import androidx.annotation.StringRes

interface Navigator {

    fun launch(screen: BaseScreen)

    fun goBack(result: Any? = null)

    fun toast(@StringRes messagesRes: Int)

    fun getString(@StringRes messagesRes: Int): String
}