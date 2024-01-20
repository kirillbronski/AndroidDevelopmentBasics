package com.kbcoding.androiddevelopmentbasics.app.ui.main.tabs.dashboard

import androidx.lifecycle.viewModelScope
import com.kbcoding.androiddevelopmentbasics.app.domain.Success
import com.kbcoding.androiddevelopmentbasics.app.domain.accounts.AccountsRepository
import com.kbcoding.androiddevelopmentbasics.app.domain.boxes.BoxesRepository
import com.kbcoding.androiddevelopmentbasics.app.domain.boxes.entities.BoxesFilter
import com.kbcoding.androiddevelopmentbasics.app.utils.MutableLiveEvent
import com.kbcoding.androiddevelopmentbasics.app.utils.logger.Logger
import com.kbcoding.androiddevelopmentbasics.app.utils.publishEvent
import com.kbcoding.androiddevelopmentbasics.app.utils.share
import com.kbcoding.core.BaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class BoxViewModel @AssistedInject constructor(
    @Assisted boxId: Long,
    private val boxesRepository: BoxesRepository,
    accountsRepository: AccountsRepository,
    logger: Logger
) : BaseViewModel(accountsRepository, logger) {

    private val _shouldExitEvent = MutableLiveEvent<Boolean>()
    val shouldExitEvent = _shouldExitEvent.share()

    init {
        viewModelScope.launch {
            boxesRepository.getBoxesAndSettings(BoxesFilter.ONLY_ACTIVE)
                .map { res -> res.map { boxes -> boxes.firstOrNull { it.box.id == boxId } } }
                .collect { res ->
                    _shouldExitEvent.publishEvent(res is Success && res.value == null)
                }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(boxId: Long): BoxViewModel
    }

}