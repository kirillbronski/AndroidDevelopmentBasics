package com.kbcoding.androiddevelopmentbasics.presentation.currentColor

import android.Manifest
import androidx.lifecycle.viewModelScope
import com.kbcoding.androiddevelopmentbasics.R
import com.kbcoding.androiddevelopmentbasics.model.colors.ColorListener
import com.kbcoding.androiddevelopmentbasics.model.colors.ColorsRepository
import com.kbcoding.androiddevelopmentbasics.model.colors.NamedColor
import com.kbcoding.androiddevelopmentbasics.presentation.changeColor.ChangeColorFragment
import com.kbcoding.core.model.PendingResult
import com.kbcoding.core.model.SuccessResult
import com.kbcoding.core.model.takeSuccess
import com.kbcoding.core.model.tasks.dispatchers.Dispatcher
import com.kbcoding.core.model.tasks.factories.TasksFactory
import com.kbcoding.core.presentation.BaseViewModel
import com.kbcoding.core.presentation.LiveResult
import com.kbcoding.core.presentation.MutableLiveResult
import com.kbcoding.core.sideEffects.dialogs.Dialogs
import com.kbcoding.core.sideEffects.dialogs.plugin.DialogConfig
import com.kbcoding.core.sideEffects.intents.Intents
import com.kbcoding.core.sideEffects.navigator.Navigator
import com.kbcoding.core.sideEffects.permissions.Permissions
import com.kbcoding.core.sideEffects.permissions.plugin.PermissionStatus
import com.kbcoding.core.sideEffects.resources.Resources
import com.kbcoding.core.sideEffects.toasts.Toasts
import kotlinx.coroutines.launch

class CurrentColorViewModel(
    private val navigator: Navigator,
    private val toasts: Toasts,
    private val resources: Resources,
    private val permissions: Permissions,
    private val intents: Intents,
    private val dialogs: Dialogs,
    private val colorsRepository: ColorsRepository,
) : BaseViewModel() {

    private val _currentColor = MutableLiveResult<NamedColor>(PendingResult())
    val currentColor: LiveResult<NamedColor> = _currentColor

    private val colorListener: ColorListener = {
        _currentColor.postValue(SuccessResult(it))
    }

    // --- example of listening results via model layer

    init {
        colorsRepository.addListener(colorListener)
        load()
    }

    override fun onCleared() {
        super.onCleared()
        colorsRepository.removeListener(colorListener)
    }

    // --- example of listening results directly from the screen

    override fun onResult(result: Any) {
        super.onResult(result)
        if (result is NamedColor) {
            val message = resources.getString(R.string.changed_color, result.name)
            toasts.toast(message)
        }
    }

    // ---

    fun changeColor() {
        val currentColor = currentColor.value.takeSuccess() ?: return
        val screen = ChangeColorFragment.Screen(currentColor.id)
        navigator.launch(screen)
    }

    /**
     * Example of using side-effect plugins
     */
    fun requestPermission() = viewModelScope.launch {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        val hasPermission = permissions.hasPermissions(permission)
        if (hasPermission) {
            dialogs.show(createPermissionAlreadyGrantedDialog())
        } else {
            when (permissions.requestPermission(permission)) {
                PermissionStatus.GRANTED -> {
                    toasts.toast(resources.getString(R.string.permissions_grated))
                }

                PermissionStatus.DENIED -> {
                    toasts.toast(resources.getString(R.string.permissions_denied))
                }

                PermissionStatus.DENIED_FOREVER -> {
                    if (dialogs.show(createAskForLaunchingAppSettingsDialog())) {
                        intents.openAppSettings()
                    }
                }
            }
        }
    }

    fun tryAgain() {
        load()
    }

    private fun load() = into(_currentColor) { colorsRepository.getCurrentColor() }

    private fun createPermissionAlreadyGrantedDialog() = DialogConfig(
        title = resources.getString(R.string.dialog_permissions_title),
        message = resources.getString(R.string.permissions_already_granted),
        positiveButton = resources.getString(R.string.action_ok)
    )

    private fun createAskForLaunchingAppSettingsDialog() = DialogConfig(
        title = resources.getString(R.string.dialog_permissions_title),
        message = resources.getString(R.string.open_app_settings_message),
        positiveButton = resources.getString(R.string.action_open),
        negativeButton = resources.getString(R.string.action_cancel)
    )
}