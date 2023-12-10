package com.kbcoding.androiddevelopmentbasics.ui

import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.kbcoding.androiddevelopmentbasics.MutableLiveEvent
import com.kbcoding.androiddevelopmentbasics.R
import com.kbcoding.androiddevelopmentbasics.adapters.UsersAdapter
import com.kbcoding.androiddevelopmentbasics.model.users.User
import com.kbcoding.androiddevelopmentbasics.model.users.repositories.UsersRepository
import com.kbcoding.androiddevelopmentbasics.publishEvent
import com.kbcoding.androiddevelopmentbasics.share
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@FlowPreview
class MainViewModel(
    private val usersRepository: UsersRepository
) : ViewModel(), UsersAdapter.Listener {

    val isErrorsEnabled: Flow<Boolean> = usersRepository.isErrorsEnabled()

    val usersFlow: Flow<PagingData<UserListItem>>

    private val searchBy = MutableLiveData("")

    private val localChanges = LocalChanges()
    private val localChangesFlow = MutableStateFlow(OnChange(localChanges))

    private val _errorEvents = MutableLiveEvent<Int>()
    val errorEvents = _errorEvents.share()

    private val _scrollEvents = MutableLiveEvent<Unit>()
    val scrollEvents = _scrollEvents.share()

    private var _invalidateEvents = MutableLiveEvent<Unit>()
    val invalidateEvents = _invalidateEvents.share()

    init {
        val originUsersFlow = searchBy.asFlow()
            // if user types text too quickly -> filtering intermediate values to avoid excess loads
            .debounce(500)
            .flatMapLatest {
                usersRepository.getPagedUsers(it)
            }
            .cachedIn(viewModelScope)

        usersFlow = combine(
            originUsersFlow,
            localChangesFlow.debounce(50),
            this::merge
        )
    }

    override fun onUserDelete(userListItem: UserListItem) {
        if (isInProgress(userListItem)) return
        viewModelScope.launch {
            try {
                setProgress(userListItem, true)
                delete(userListItem)
            } catch (e: Exception) {
                showError(R.string.error_delete)
            } finally {
                setProgress(userListItem, false)
            }
        }
    }

    override fun onToggleFavoriteFlag(userListItem: UserListItem) {
        if (isInProgress(userListItem)) return
        viewModelScope.launch {
            try {
                setProgress(userListItem, true)
                setFavoriteFlag(userListItem)
            } catch (e: Exception) {
                showError(R.string.error_change_favorite)
            } finally {
                setProgress(userListItem, false)
            }
        }
    }

    fun setSearchBy(value: String) {
        if (this.searchBy.value == value) return
        this.searchBy.value = value
        scrollListToTop()
    }

    fun refresh() {
        this.searchBy.postValue(this.searchBy.value)
    }

    fun setEnableErrors(value: Boolean) {
        // called when 'Enable Errors' checkbox value is changed
        usersRepository.setErrorsEnabled(value)
    }

    private fun setProgress(userListItem: UserListItem, inProgress: Boolean) {
        if (inProgress) {
            localChanges.idsInProgress.add(userListItem.id)
        } else {
            localChanges.idsInProgress.remove(userListItem.id)
        }
        localChangesFlow.value = OnChange(localChanges)
    }

    private fun isInProgress(userListItem: UserListItem) =
        localChanges.idsInProgress.contains(userListItem.id)

    private suspend fun setFavoriteFlag(userListItem: UserListItem) {
        val newFlagValue = !userListItem.isFavorite
        usersRepository.setIsFavorite(userListItem.user, newFlagValue)
        localChanges.favoriteFlags[userListItem.id] = newFlagValue
        localChangesFlow.value = OnChange(localChanges)
    }

    private suspend fun delete(userListItem: UserListItem) {
        usersRepository.delete(userListItem.user)
        invalidateList()
    }

    private fun showError(@StringRes errorMessage: Int) {
        _errorEvents.publishEvent(errorMessage)
    }

    private fun scrollListToTop() {
        _scrollEvents.publishEvent(Unit)
    }

    private fun invalidateList() {
        _invalidateEvents.publishEvent(Unit)
    }

    private fun merge(users: PagingData<User>, localChanges: OnChange<LocalChanges>): PagingData<UserListItem> {
        return users
            .map { user ->
                val isInProgress = localChanges.value.idsInProgress.contains(user.id)
                val localFavoriteFlag = localChanges.value.favoriteFlags[user.id]

                val userWithLocalChanges = if (localFavoriteFlag == null) {
                    user
                } else {
                    user.copy(isFavorite = localFavoriteFlag)
                }
                UserListItem(userWithLocalChanges, isInProgress)
            }
    }

    /**
     * Non-data class which allows passing the same reference to the
     * MutableStateFlow multiple times in a row.
     */
    class OnChange<T>(val value: T)

    /**
     * Contains:
     * 1) identifiers of items which are processed now (deleting or favorite
     * flag updating).
     * 2) local favorite flag updates to avoid list reloading
     */
    class LocalChanges {
        val idsInProgress = mutableSetOf<Long>()
        val favoriteFlags = mutableMapOf<Long, Boolean>()
    }

}