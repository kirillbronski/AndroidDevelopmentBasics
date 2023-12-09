package com.kbcoding.androiddevelopmentbasics.ui.main.tabs.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kbcoding.androiddevelopmentbasics.R
import com.kbcoding.androiddevelopmentbasics.model.boxes.entities.Box
import com.kbcoding.androiddevelopmentbasics.domain.repository.BoxesRepository
import com.kbcoding.androiddevelopmentbasics.model.StorageException
import com.kbcoding.androiddevelopmentbasics.model.boxes.entities.BoxAndSettings
import com.kbcoding.androiddevelopmentbasics.utils.MutableLiveEvent
import com.kbcoding.androiddevelopmentbasics.utils.publishEvent
import com.kbcoding.androiddevelopmentbasics.utils.share
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val boxesRepository: BoxesRepository
) : ViewModel(), SettingsAdapter.Listener {

    private val _boxSettings = MutableLiveData<List<BoxAndSettings>>()
    val boxSettings = _boxSettings.share()

    private val _showErrorMessageEvent = MutableLiveEvent<Int>()
    val showErrorMessageEvent =_showErrorMessageEvent.share()

    init {
        viewModelScope.launch {
            boxesRepository.getBoxesAndSettings().collect {
                _boxSettings.value = it
            }
        }
    }

    override fun enableBox(box: Box) {
        viewModelScope.launch {
            try {
                boxesRepository.activateBox(box)
            } catch (e: StorageException) {
                showStorageErrorMessage()
            }
        }
    }

    override fun disableBox(box: Box) {
        viewModelScope.launch {
            try {
                boxesRepository.deactivateBox(box)
            } catch (e: StorageException) {
                showStorageErrorMessage()
            }
        }
    }

    private fun showStorageErrorMessage() {
        _showErrorMessageEvent.publishEvent(R.string.storage_error)
    }
}