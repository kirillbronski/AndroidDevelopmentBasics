package com.kbcoding.core.navigator

import com.kbcoding.core.presentation.BaseScreen
import com.kbcoding.core.utils.ResourceActions

class IntermediateNavigator : Navigator {

    private val targetNavigator = ResourceActions<Navigator>()
    override fun launch(screen: BaseScreen) = targetNavigator {
        it.launch(screen)
    }

    override fun goBack(result: Any?) = targetNavigator {
        it.goBack(result)
    }

    fun setTarget(navigator: Navigator?) {
        targetNavigator.resource = navigator
    }

    fun clear() {
        targetNavigator.clear()
    }
}