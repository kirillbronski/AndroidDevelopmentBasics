package com.kbcoding.core.sideEffects.toasts.plugin

import android.content.Context
import com.kbcoding.core.sideEffects.SideEffectMediator
import com.kbcoding.core.sideEffects.SideEffectPlugin
import com.kbcoding.core.sideEffects.toasts.Toasts

class ToastsPlugin : SideEffectPlugin<Toasts, Nothing> {

    override val mediatorClass: Class<Toasts>
        get() = Toasts::class.java

    override fun createMediator(applicationContext: Context): SideEffectMediator<Nothing> {
        return ToastsSideEffectMediator(applicationContext)
    }

}