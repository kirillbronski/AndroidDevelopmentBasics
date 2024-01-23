package com.kbcoding.uialertsimpl

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.kbcoding.uialerts.UiAlerts

class DefaultUiAlerts(
    private val context: Context,
) : UiAlerts {

    private val handler = Handler(Looper.getMainLooper())

    override fun toast(message: String) {
        handler.post {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

}