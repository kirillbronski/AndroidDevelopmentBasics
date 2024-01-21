package com.kbcoding.androiddevelopmentbasics.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.kbcoding.androiddevelopmentbasics.model.Cat
import com.kbcoding.androiddevelopmentbasics.model.CatsRepository
import com.kbcoding.androiddevelopmentbasics.viewModel.base.BaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class CatDetailsViewModel @AssistedInject constructor(
    private val catsRepository: CatsRepository,
    @Assisted catId: Long
) : BaseViewModel() {

    val catLiveData: LiveData<Cat> = liveData()

    init {
        viewModelScope.launch {
            catsRepository.getCatById(catId).filterNotNull().collect {
                catLiveData.update(it)
            }
        }
    }

    fun toggleFavorite() {
        val cat = catLiveData.value ?: return
        catsRepository.toggleIsFavorite(cat)
    }

    @AssistedFactory
    interface Factory {
        fun create(catId: Long): CatDetailsViewModel
    }

}