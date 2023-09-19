package com.kbcoding.core.presentation

import com.kbcoding.core.ActivityScopeViewModel

/**
 * Implement this interface in the activity.
 */
interface FragmentsHolder {

    /**
     * Called when activity views should be re-drawn.
     */
    fun notifyScreenUpdates()

    /**
     * Get the current implementations of dependencies from activity VM scope.
     */
    fun getActivityScopeViewModel(): ActivityScopeViewModel

}