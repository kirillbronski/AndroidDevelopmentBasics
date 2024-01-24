package com.kbcoding.core.sideEffects.dialogs.plugin

import android.content.Context
import com.kbcoding.core.sideEffects.SideEffectMediator
import com.kbcoding.core.sideEffects.SideEffectPlugin

/**
 * Plugin for displaying dialogs from view-models.
 * Allows adding [Dialogs] interface to view-model constructor.
 */
class DialogsPlugin : SideEffectPlugin<DialogsSideEffectMediator, DialogsSideEffectImpl> {

    override val mediatorClass: Class<DialogsSideEffectMediator>
        get() = DialogsSideEffectMediator::class.java

    override fun createMediator(applicationContext: Context): SideEffectMediator<DialogsSideEffectImpl> {
        return DialogsSideEffectMediator()
    }

    override fun createImplementation(mediator: DialogsSideEffectMediator): DialogsSideEffectImpl {
        return DialogsSideEffectImpl(mediator.retainedState)
    }
}