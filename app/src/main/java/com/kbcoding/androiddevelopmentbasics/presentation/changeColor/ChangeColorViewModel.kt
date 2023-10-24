package com.kbcoding.androiddevelopmentbasics.presentation.changeColor


import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import com.kbcoding.androiddevelopmentbasics.R
import com.kbcoding.androiddevelopmentbasics.model.colors.ColorsRepository
import com.kbcoding.androiddevelopmentbasics.model.colors.NamedColor
import com.kbcoding.core.model.PendingResult
import com.kbcoding.core.model.Progress
import com.kbcoding.core.model.Result
import com.kbcoding.core.model.SuccessResult
import com.kbcoding.core.model.getPercentage
import com.kbcoding.core.model.isInProgress
import com.kbcoding.core.presentation.BaseViewModel
import com.kbcoding.core.presentation.ResultMutableStateFlow
import com.kbcoding.core.sideEffects.navigator.Navigator
import com.kbcoding.core.sideEffects.resources.Resources
import com.kbcoding.core.sideEffects.toasts.Toasts
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ChangeColorViewModel(
    screen: ChangeColorFragment.Screen,
    private val navigator: Navigator,
    private val toasts: Toasts,
    private val resources: Resources,
    private val colorsRepository: ColorsRepository,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel(), ColorsAdapter.Listener {

    // input sources
    private val _availableColors: ResultMutableStateFlow<List<NamedColor>> =
        MutableStateFlow(PendingResult())

    private val _currentColorId =
        savedStateHandle.getStateFlowExt("currentColorId", screen.currentColorId)

    private val _saveInProgress = MutableStateFlow<Progress>(Progress.EmptyProgress)

    // main destination (contains merged values from _availableColors & _currentColorId)
    val viewState: Flow<Result<ViewState>> = combine(
        _availableColors,
        _currentColorId,
        _saveInProgress,
        ::mergeSources
    )

    val screenTitle: LiveData<String> = viewState
        .map { result ->
            return@map if (result is SuccessResult) {
                val currentColor = result.data.colorsList.first { it.selected }
                resources.getString(
                    R.string.change_color_screen_title,
                    currentColor.namedColor.name
                )
            } else {
                resources.getString(R.string.change_color_screen_title_simple)
            }
        }
        .asLiveData()

    init {
        load()
    }

    override fun onColorChosen(namedColor: NamedColor) {
        if (_saveInProgress.value.isInProgress()) return
        _currentColorId.value = namedColor.id
    }

    fun onSavePressed() = viewModelScope.launch {
        try {
            _saveInProgress.value = Progress.START

            val currentColorId =
                _currentColorId.value ?: throw IllegalStateException("Color ID should not be NULL")
            val currentColor = colorsRepository.getById(currentColorId)
            colorsRepository.setCurrentColor(currentColor).collect()

            navigator.goBack(currentColor)
        } catch (e: Exception) {
            if (e !is CancellationException) toasts.toast(resources.getString(R.string.error_happened))
        } finally {
            _saveInProgress.value = Progress.EmptyProgress
        }

    }

    fun onCancelPressed() {
        navigator.goBack()
    }

    fun tryAgain() {
        load()
    }

    /**
     * [MediatorLiveData] can listen other LiveData instances (even more than 1)
     * and combine their values.
     * Here we listen the list of available colors ([_availableColors] live-data) + current color id
     * ([_currentColorId] live-data) + whether save is in progress or not, then we use all of
     * these values in order to create a [ViewState] instance, which is in its turn rendered by fragment.
     */
    private fun mergeSources(
        colors: Result<List<NamedColor>>,
        currentColorId: Long,
        saveInProgress: Progress
    ): Result<ViewState> {
        // map Result<List<NamedColor>> to Result<ViewState>
        return colors.map { colorsList ->
            ViewState(
                // map List<NamedColor> to List<NamedColorListItem>
                colorsList = colorsList.map { NamedColorListItem(it, currentColorId == it.id) },

                showSaveButton = !saveInProgress.isInProgress(),
                showCancelButton = !saveInProgress.isInProgress(),
                showSaveProgressBar = saveInProgress.isInProgress(),

                saveProgressPercentage = saveInProgress.getPercentage(),
                saveProgressPercentageMessage =
                resources.getString(R.string.percentage_value, saveInProgress.getPercentage())
            )
        }
    }

    private fun load() = into(_availableColors) { colorsRepository.getAvailableColors() }

    data class ViewState(
        val colorsList: List<NamedColorListItem>,
        val showSaveButton: Boolean,
        val showCancelButton: Boolean,
        val showSaveProgressBar: Boolean,

        val saveProgressPercentage: Int,
        val saveProgressPercentageMessage: String
    )

}