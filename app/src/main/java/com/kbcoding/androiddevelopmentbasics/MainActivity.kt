package com.kbcoding.androiddevelopmentbasics

import android.os.Bundle
import com.kbcoding.androiddevelopmentbasics.presentation.currentColor.CurrentColorFragment
import com.kbcoding.core.presentation.activity.BaseActivity
import com.kbcoding.core.sideEffects.SideEffectPluginsManager
import com.kbcoding.core.sideEffects.dialogs.plugin.DialogsPlugin
import com.kbcoding.core.sideEffects.intents.plugin.IntentsPlugin
import com.kbcoding.core.sideEffects.navigator.plugin.NavigatorPlugin
import com.kbcoding.core.sideEffects.navigator.plugin.StackFragmentNavigator
import com.kbcoding.core.sideEffects.permissions.plugin.PermissionsPlugin
import com.kbcoding.core.sideEffects.resources.plugin.ResourcesPlugin
import com.kbcoding.core.sideEffects.toasts.plugin.ToastsPlugin

class MainActivity : BaseActivity() {

    override fun registerPlugins(manager: SideEffectPluginsManager) = with (manager) {
        val navigator = createNavigator()
        register(ToastsPlugin())
        register(ResourcesPlugin())
        register(NavigatorPlugin(navigator))
        register(PermissionsPlugin())
        register(DialogsPlugin())
        register(IntentsPlugin())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun createNavigator() = StackFragmentNavigator(
        containerId = R.id.fc_main_container,
        defaultTitle = getString(R.string.app_name),
        animations = StackFragmentNavigator.Animations(
            enterAnim = R.anim.enter,
            exitAnim = R.anim.exit,
            popEnterAnim = R.anim.pop_enter,
            popExitAnim = R.anim.pop_exit
        ),
        initialScreenCreator = { CurrentColorFragment.Screen() }
    )

}