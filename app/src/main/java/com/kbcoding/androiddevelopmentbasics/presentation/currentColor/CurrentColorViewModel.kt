package com.kbcoding.androiddevelopmentbasics.presentation.currentColor

import androidx.lifecycle.viewModelScope
import com.kbcoding.androiddevelopmentbasics.R
import com.kbcoding.androiddevelopmentbasics.model.colors.ColorListener
import com.kbcoding.androiddevelopmentbasics.model.colors.ColorsRepository
import com.kbcoding.androiddevelopmentbasics.model.colors.NamedColor
import com.kbcoding.androiddevelopmentbasics.presentation.changeColor.ChangeColorFragment
import com.kbcoding.core.model.ErrorResult
import com.kbcoding.core.model.PendingResult
import com.kbcoding.core.model.SuccessResult
import com.kbcoding.core.model.takeSuccess
import com.kbcoding.core.navigator.Navigator
import com.kbcoding.core.presentation.BaseViewModel
import com.kbcoding.core.presentation.LiveResult
import com.kbcoding.core.presentation.MutableLiveResult
import com.kbcoding.core.uiActions.UiActions
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CurrentColorViewModel(
    private val navigator: Navigator,
    private val uiActions: UiActions,
    private val colorsRepository: ColorsRepository
) : BaseViewModel() {

    private val _currentColor = MutableLiveResult<NamedColor>(PendingResult())
    val currentColor: LiveResult<NamedColor> = _currentColor

    private val colorListener: ColorListener = {
        _currentColor.postValue(SuccessResult(it))
    }

    // --- example of listening results via model layer

    init {
        viewModelScope.launch {
            delay(2000)
            colorsRepository.addListener(colorListener)
        }

    }

    override fun onCleared() {
        super.onCleared()
        colorsRepository.removeListener(colorListener)
    }

    // --- example of listening results directly from the screen

    override fun onResult(result: Any) {
        super.onResult(result)
        if (result is NamedColor) {
            val message = uiActions.getString(R.string.changed_color, result.name)
            uiActions.toast(message)
        }
    }

    // ---

    fun changeColor() {
        val currentColor = currentColor.value.takeSuccess() ?: return
        val screen = ChangeColorFragment.Screen(currentColor.id)
        navigator.launch(screen)
    }

    fun tryAgain() {
        viewModelScope.launch {
            _currentColor.postValue(PendingResult())
            delay(2000)
            colorsRepository.addListener(colorListener)
        }
    }

}