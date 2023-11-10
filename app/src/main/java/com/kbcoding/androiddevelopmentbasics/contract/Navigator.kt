package com.kbcoding.androiddevelopmentbasics.contract

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.kbcoding.androiddevelopmentbasics.Options
import java.io.Serializable

typealias ResultListener<T> = (T) -> Unit

fun Fragment.navigator(): Navigator {
    return requireActivity() as Navigator
}

interface Navigator {

    fun showBoxSelectionScreen(options: Options)

    fun showOptionsScreen(options: Options)

    fun showAboutScreen()

    fun showCongratulationsScreen()

    fun goBack()

    fun goToMenu()

    fun <T : Serializable> publishResult(result: T)
    fun <T : Serializable> listenResult(
        owner: LifecycleOwner,
        listener: ResultListener<T>
    )
}