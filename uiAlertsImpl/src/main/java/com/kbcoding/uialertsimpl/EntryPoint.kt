package com.kbcoding.uialertsimpl

import android.content.Context
import com.kbcoding.uialerts.UiAlerts

@Suppress("unused")
object EntryPoint {

    @JvmStatic
    fun get(context: Context): UiAlerts {
        return DefaultUiAlerts(context)
    }

}