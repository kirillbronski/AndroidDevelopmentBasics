package com.kbcoding.core.sideEffects.resources.plugin

import android.content.Context
import com.kbcoding.core.sideEffects.SideEffectMediator
import com.kbcoding.core.sideEffects.resources.Resources

class ResourcesSideEffectMediator(
    private val appContext: Context
) : SideEffectMediator<Nothing>(), Resources {

    override fun getString(resId: Int, vararg args: Any): String {
        return appContext.getString(resId, *args)
    }

}