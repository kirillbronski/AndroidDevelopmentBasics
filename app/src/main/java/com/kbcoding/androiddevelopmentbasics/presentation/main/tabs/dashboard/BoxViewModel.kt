package com.kbcoding.androiddevelopmentbasics.presentation.main.tabs.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kbcoding.androiddevelopmentbasics.domain.repository.BoxesRepository
import com.kbcoding.androiddevelopmentbasics.utils.MutableLiveEvent
import com.kbcoding.androiddevelopmentbasics.utils.publishEvent
import com.kbcoding.androiddevelopmentbasics.utils.share
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class BoxViewModel(
    private val boxId: Int,
    private val boxesRepository: BoxesRepository
) : ViewModel() {

    private val _shouldExitEvent = MutableLiveEvent<Boolean>()
    val shouldExitEvent = _shouldExitEvent.share()

    init {
        viewModelScope.launch {
            boxesRepository.getBoxes(onlyActive = true)
                .map { boxes -> boxes.firstOrNull { it.id == boxId } }
                .collect { currentBox ->
                    _shouldExitEvent.publishEvent(currentBox == null)
                }
        }
    }
}