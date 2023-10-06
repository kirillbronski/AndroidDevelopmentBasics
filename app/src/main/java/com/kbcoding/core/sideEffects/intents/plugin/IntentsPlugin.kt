package com.kbcoding.core.sideEffects.intents.plugin

import android.content.Context
import com.kbcoding.core.sideEffects.SideEffectMediator
import com.kbcoding.core.sideEffects.SideEffectPlugin
import com.kbcoding.core.sideEffects.intents.Intents

/**
 * Plugin for launching system activities from view-models.
 * Allows adding [Intents] interface to the view-model constructor.
 */
class IntentsPlugin : SideEffectPlugin<Intents, Nothing> {

    override val mediatorClass: Class<Intents>
        get() = Intents::class.java

    override fun createMediator(applicationContext: Context): SideEffectMediator<Nothing> {
        return IntentsSideEffectMediator(applicationContext)
    }

}