package com.kbcoding.androiddevelopmentbasics.ui

import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.kbcoding.androiddevelopmentbasics.R
import com.kbcoding.androiddevelopmentbasics.domain.Launch
import com.kbcoding.androiddevelopmentbasics.domain.LaunchesRepository
import com.kbcoding.androiddevelopmentbasics.ui.base.MutableLiveEvent
import com.kbcoding.androiddevelopmentbasics.ui.base.publishEvent
import com.kbcoding.androiddevelopmentbasics.ui.base.share
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class MainViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val launchesRepository: LaunchesRepository
) : ViewModel() {

    private val _toastEvent = MutableLiveEvent<Int>()
    val toastEvent = _toastEvent.share()

    private val selections = Selections()

    private val yearLiveData =
        savedStateHandle.getLiveData(KEY_YEAR, 2020)
    var year: Int?
        get() = yearLiveData.value
        set(value) {
            yearLiveData.value = value
        }

    private val launchesFlow = yearLiveData.asFlow()
        .distinctUntilChanged()
        .flatMapLatest {
            launchesRepository.getLaunches(it)
        }
        .cachedIn(viewModelScope)

    val launchesListFlow = combine(
            launchesFlow,
            selections.flow(),
            ::merge
        )

    fun toggleCheckState(launch: LaunchUiEntity) {
        selections.toggle(launch.id)
    }

    fun toggleSuccessFlag(launch: LaunchUiEntity) = viewModelScope.launch {
        try {
            launchesRepository.toggleSuccessFlag(launch)
        } catch (e: Exception) {
            showToast(R.string.oops)
        }
    }

    private fun merge(
        pagingData: PagingData<Launch>,
        selections: SelectionState
    ): PagingData<LaunchUiEntity> {
        return pagingData.map { launch ->
            LaunchUiEntity(
                launch = launch,
                isChecked = selections.isChecked(launch.id)
            )
        }
    }

    private fun showToast(@StringRes messageRes: Int) {
        _toastEvent.publishEvent(messageRes)
    }

    private companion object {
        const val KEY_YEAR = "KEY_YEAR"
    }

}