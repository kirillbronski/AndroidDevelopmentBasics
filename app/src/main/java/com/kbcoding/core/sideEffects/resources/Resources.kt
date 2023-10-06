package com.kbcoding.core.sideEffects.resources

import androidx.annotation.StringRes

/**
 * Interface for accessing resources from view-models.
 * You need to add [ResourcesPlugin] to your activity before using this feature.
 */
interface Resources {

    fun getString(@StringRes resId: Int, vararg args: Any): String

}