package com.kbcoding.androiddevelopmentbasics.screens.hello

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kbcoding.androiddevelopmentbasics.R
import com.kbcoding.androiddevelopmentbasics.core.base.BaseViewModel
import com.kbcoding.androiddevelopmentbasics.core.navigator.Navigator
import com.kbcoding.androiddevelopmentbasics.screens.edit.EditFragment

class HelloViewModel(
    private val navigator: Navigator,
    screen: HelloFragment.Screen
): BaseViewModel() {

    private val _currentMessageLiveData = MutableLiveData<String>()
    val currentMessageLiveData: LiveData<String> = _currentMessageLiveData

    init {
        _currentMessageLiveData.value = navigator.getString(R.string.hello_world)
    }

    override fun onResult(result: Any) {
        if (result is String) {
            _currentMessageLiveData.value = result
        }
    }

    fun onEditPressed() {
        navigator.launch(EditFragment.Screen(initialValue = currentMessageLiveData.value!!))
    }
}