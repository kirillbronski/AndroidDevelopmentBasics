package com.kbcoding.core.sideEffects.toasts.plugin

import android.content.Context
import android.widget.Toast
import com.kbcoding.core.model.dispatchers.MainThreadDispatcher
import com.kbcoding.core.sideEffects.SideEffectMediator
import com.kbcoding.core.sideEffects.toasts.Toasts

/**
 * Android implementation of [Toasts]. Displaying simple toast message and getting string from resources.
 */
class ToastsSideEffectMediator(
    private val appContext: Context
) : SideEffectMediator<Nothing>(), Toasts {

    private val dispatcher = MainThreadDispatcher()

    override fun toast(message: String) {
        dispatcher.dispatch {
            Toast.makeText(appContext, message, Toast.LENGTH_SHORT).show()
        }
    }

}