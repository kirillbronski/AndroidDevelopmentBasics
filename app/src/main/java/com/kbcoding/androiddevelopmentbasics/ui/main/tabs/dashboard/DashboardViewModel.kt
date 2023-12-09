package com.kbcoding.androiddevelopmentbasics.ui.main.tabs.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kbcoding.androiddevelopmentbasics.model.boxes.entities.Box
import com.kbcoding.androiddevelopmentbasics.domain.repository.BoxesRepository
import com.kbcoding.androiddevelopmentbasics.utils.share
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val boxesRepository: BoxesRepository
) : ViewModel() {

    private val _boxes = MutableLiveData<List<Box>>()
    val boxes = _boxes.share()

    init {
        viewModelScope.launch {
            boxesRepository.getBoxesAndSettings(onlyActive = true).collect { list ->
                _boxes.value = list.map { it.box }
            }
        }
    }

}