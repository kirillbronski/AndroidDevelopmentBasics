package com.kbcoding.androiddevelopmentbasics.presentation.currentColor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kbcoding.androiddevelopmentbasics.R
import com.kbcoding.androiddevelopmentbasics.model.colors.ColorListener
import com.kbcoding.androiddevelopmentbasics.model.colors.ColorsRepository
import com.kbcoding.androiddevelopmentbasics.model.colors.NamedColor
import com.kbcoding.core.navigator.Navigator
import com.kbcoding.core.uiActions.UiActions
import com.kbcoding.core.presentation.BaseViewModel
import com.kbcoding.androiddevelopmentbasics.presentation.changeColor.ChangeColorFragment

class CurrentColorViewModel(
    private val navigator: Navigator,
    private val uiActions: UiActions,
    private val colorsRepository: ColorsRepository
) : BaseViewModel() {

    private val _currentColor = MutableLiveData<NamedColor>()
    val currentColor: LiveData<NamedColor> = _currentColor

    private val colorListener: ColorListener = {
        _currentColor.postValue(it)
    }

    // --- example of listening results via model layer

    init {
        colorsRepository.addListener(colorListener)
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
        val currentColor = currentColor.value ?: return
        val screen = ChangeColorFragment.Screen(currentColor.id)
        navigator.launch(screen)
    }

}