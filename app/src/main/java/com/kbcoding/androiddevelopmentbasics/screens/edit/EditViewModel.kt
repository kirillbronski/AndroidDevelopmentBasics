package com.kbcoding.androiddevelopmentbasics.screens.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kbcoding.androiddevelopmentbasics.R
import com.kbcoding.androiddevelopmentbasics.core.Event
import com.kbcoding.androiddevelopmentbasics.core.base.BaseViewModel
import com.kbcoding.androiddevelopmentbasics.core.navigator.Navigator

class EditViewModel(
    private val navigator: Navigator,
    screen: EditFragment.Screen
) : BaseViewModel() {

    private val _initialMessageEvent = MutableLiveData<Event<String>>()
    val initialMessageEvent: LiveData<Event<String>> = _initialMessageEvent

    init {
        _initialMessageEvent.value = Event(screen.initialValue)
    }

    fun onSavePressed(message: String) {
        if (message.isBlank()) {
            navigator.toast(R.string.empty_message)
            return
        }
        navigator.goBack(message)
    }

    fun onCancelPressed() {
        navigator.goBack()
    }

}