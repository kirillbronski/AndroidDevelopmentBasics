package com.kbcoding.androiddevelopmentbasics.core

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kbcoding.androiddevelopmentbasics.core.base.BaseFragment
import com.kbcoding.androiddevelopmentbasics.core.navigator.ARG_SCREEN
import com.kbcoding.androiddevelopmentbasics.core.navigator.BaseScreen
import com.kbcoding.androiddevelopmentbasics.core.navigator.MainNavigator
import com.kbcoding.androiddevelopmentbasics.core.navigator.Navigator

class ViewModelFactory(
    private val screen: BaseScreen,
    private val fragment: BaseFragment<*>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val hostActivity = fragment.requireActivity()
        val application = hostActivity.application
        val navigatorProvider = ViewModelProvider(
            hostActivity,
            ViewModelProvider.AndroidViewModelFactory(application)
        )
        val navigator = navigatorProvider[MainNavigator::class.java]
        // if you need to create a view model with some other arguments -> you may
        // use 'constructors' field for searching the desired constructor
        val constructor = modelClass.getConstructor(Navigator::class.java, screen::class.java)
        return constructor.newInstance(navigator, screen)
    }

}

/**
 * Use this method for getting view-models from your fragments
 */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
inline fun <reified VM : ViewModel> BaseFragment<*>.screenViewModel() = viewModels<VM> {
    val screen = requireArguments().getParcelable(ARG_SCREEN, BaseScreen::class.java) as BaseScreen
    ViewModelFactory(screen, this)
}