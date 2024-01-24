package com.kbcoding.core.sideEffects.navigator.plugin

import com.kbcoding.core.presentation.BaseScreen
import com.kbcoding.core.sideEffects.SideEffectMediator
import com.kbcoding.core.sideEffects.navigator.Navigator

class NavigatorSideEffectMediator : SideEffectMediator<Navigator>(), Navigator {

    override fun launch(screen: BaseScreen) = target {
        it.launch(screen)
    }

    override fun goBack(result: Any?) = target {
        it.goBack(result)
    }

}
