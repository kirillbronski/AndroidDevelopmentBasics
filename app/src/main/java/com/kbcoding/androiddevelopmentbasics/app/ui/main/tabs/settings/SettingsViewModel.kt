package com.kbcoding.androiddevelopmentbasics.app.ui.main.tabs.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kbcoding.androiddevelopmentbasics.app.model.ResponseResult
import com.kbcoding.androiddevelopmentbasics.app.model.accounts.AccountsRepository
import com.kbcoding.androiddevelopmentbasics.app.model.boxes.BoxesRepository
import com.kbcoding.androiddevelopmentbasics.app.model.boxes.entities.Box
import com.kbcoding.androiddevelopmentbasics.app.model.boxes.entities.BoxAndSettings
import com.kbcoding.androiddevelopmentbasics.app.model.boxes.entities.BoxesFilter
import com.kbcoding.androiddevelopmentbasics.app.utils.logger.LogCatLogger
import com.kbcoding.androiddevelopmentbasics.app.utils.logger.Logger
import com.kbcoding.androiddevelopmentbasics.app.utils.share
import com.kbcoding.core.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val boxesRepository: BoxesRepository,
    accountsRepository: AccountsRepository,
    logger: Logger
) : BaseViewModel(accountsRepository, logger), SettingsAdapter.Listener {

    private val _boxSettings = MutableLiveData<ResponseResult<List<BoxAndSettings>>>()
    val boxSettings = _boxSettings.share()

    init {
        viewModelScope.launch {
            boxesRepository.getBoxesAndSettings(BoxesFilter.ALL).collect {
                _boxSettings.value = it
            }
        }
    }

    fun tryAgain() = viewModelScope.safeLaunch {
        boxesRepository.reload(BoxesFilter.ALL)
    }

    override fun enableBox(box: Box) = viewModelScope.safeLaunch {
        boxesRepository.activateBox(box)
    }

    override fun disableBox(box: Box) = viewModelScope.safeLaunch {
        boxesRepository.deactivateBox(box)
    }

}