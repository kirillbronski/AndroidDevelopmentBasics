package com.kbcoding.androiddevelopmentbasics.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kbcoding.androiddevelopmentbasics.model.users.User
import com.kbcoding.androiddevelopmentbasics.model.users.repositories.UsersRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class MainViewModel(
    private val usersRepository: UsersRepository
) : ViewModel() {

    val isErrorsEnabled: Flow<Boolean> = usersRepository.isErrorsEnabled()

    val usersFlow: Flow<PagingData<User>>

    private val searchBy = MutableLiveData("")

    init {
        usersFlow = searchBy.asFlow()
            // if user types text too quickly -> filtering intermediate values to avoid excess loads
            .debounce(500)
            .flatMapLatest {
                usersRepository.getPagedUsers(it)
            }
            // always use cacheIn operator for flows returned by Pager. Otherwise exception may be thrown
            // when 1) refreshing/invalidating or 2) subscribing to the flow more than once.
            .cachedIn(viewModelScope)
    }

    fun setSearchBy(value: String) {
        if (this.searchBy.value == value) return
        this.searchBy.value = value
    }

    fun refresh() {
        this.searchBy.postValue(this.searchBy.value)
    }

    fun setEnableErrors(value: Boolean) {
        // called when 'Enable Errors' checkbox value is changed
        usersRepository.setErrorsEnabled(value)
    }

}