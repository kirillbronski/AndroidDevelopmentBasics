package com.kbcoding.core.sideEffects.permissions.plugin

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.kbcoding.core.model.ErrorResult
import com.kbcoding.core.model.tasks.Task
import com.kbcoding.core.model.tasks.callback.CallbackTask
import com.kbcoding.core.model.tasks.callback.Emitter
import com.kbcoding.core.sideEffects.SideEffectMediator
import com.kbcoding.core.sideEffects.permissions.Permissions

class PermissionsSideEffectMediator(
    private val appContext: Context
) : SideEffectMediator<PermissionsSideEffectImpl>(), Permissions {

    val retainedState = RetainedState()

    override fun hasPermissions(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(appContext, permission) == PackageManager.PERMISSION_GRANTED
    }

    override suspend fun requestPermission(permission: String): PermissionStatus = CallbackTask.create<PermissionStatus> { emitter ->
        if (retainedState.emitter != null) {
            emitter.emit(ErrorResult(IllegalStateException("Only one permission request can be active")))
            return@create
        }
        retainedState.emitter = emitter
        target { implementation ->
            implementation.requestPermission(permission)
        }
    }.suspend()

    class RetainedState(
        var emitter: Emitter<PermissionStatus>? = null
    )

}