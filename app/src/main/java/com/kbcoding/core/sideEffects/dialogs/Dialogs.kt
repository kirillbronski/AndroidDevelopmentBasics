package com.kbcoding.core.sideEffects.dialogs

import com.kbcoding.core.model.tasks.Task
import com.kbcoding.core.sideEffects.dialogs.plugin.DialogConfig

/**
 * Side-effects interface for managing dialogs from view-model.
 * You need to add [DialogsPlugin] to your activity before using this feature.
 *
 * WARN! Please note, dialogs don't survive after app killing.
 */
interface Dialogs {

    /**
     * Show alert dialog to the user and wait for the user choice.
     */
    suspend fun show(dialogConfig: DialogConfig): Boolean

}