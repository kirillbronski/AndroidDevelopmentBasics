package com.kbcoding.core.sideEffects.permissions

import com.kbcoding.core.model.tasks.Task
import com.kbcoding.core.sideEffects.permissions.plugin.PermissionStatus

/**
 * Side-effects interface for managing permissions from view-model.
 * You need to add [PermissionsPlugin] to your activity before using this feature.
 *
 * WARNING! Please note that such usage of permissions requests doesn't allow you to handle
 * responses after app killing.
 */
interface Permissions {

    /**
     * Whether the app has the specified permission or not.
     */
    fun hasPermissions(permission: String): Boolean

    /**
     * Request the specified permission.
     * See [PermissionStatus]
     */
    fun requestPermission(permission: String): Task<PermissionStatus>

}