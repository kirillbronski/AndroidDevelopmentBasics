package com.kbcoding.androiddevelopmentbasics.app.ui.main.tabs.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kbcoding.androiddevelopmentbasics.app.domain.ResponseResult
import com.kbcoding.androiddevelopmentbasics.app.domain.accounts.AccountsRepository
import com.kbcoding.androiddevelopmentbasics.app.domain.boxes.BoxesRepository
import com.kbcoding.androiddevelopmentbasics.app.domain.boxes.entities.Box
import com.kbcoding.androiddevelopmentbasics.app.domain.boxes.entities.BoxesFilter
import com.kbcoding.androiddevelopmentbasics.app.utils.logger.Logger
import com.kbcoding.androiddevelopmentbasics.app.utils.share
import com.kbcoding.core.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val boxesRepository: BoxesRepository,
    accountsRepository: AccountsRepository,
    logger: Logger
) : BaseViewModel(accountsRepository, logger) {

    private val _boxes = MutableLiveData<ResponseResult<List<Box>>>()
    val boxes = _boxes.share()

    init {
        viewModelScope.launch {
            boxesRepository.getBoxesAndSettings(BoxesFilter.ONLY_ACTIVE).collect { result ->
                _boxes.value = result.map { list -> list.map { it.box } }
            }
        }
    }

    fun reload() = viewModelScope.launch {
        boxesRepository.reload(BoxesFilter.ONLY_ACTIVE)
    }

}