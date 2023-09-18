package com.kbcoding.androiddevelopmentbasics.presentation.users.details

import android.content.BroadcastReceiver.PendingResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kbcoding.androiddevelopmentbasics.R
import com.kbcoding.androiddevelopmentbasics.core.UserNotFoundException
import com.kbcoding.androiddevelopmentbasics.core.presentation.BaseViewModel
import com.kbcoding.androiddevelopmentbasics.core.presentation.Event
import com.kbcoding.androiddevelopmentbasics.core.result.Result
import com.kbcoding.androiddevelopmentbasics.model.UserDetails
import com.kbcoding.androiddevelopmentbasics.model.UsersService

class UserDetailsViewModel(
    private val usersService: UsersService
) : BaseViewModel() {

    private val _state = MutableLiveData<State>()
    val state: LiveData<State> get() = _state

    private val _actionShowToast = MutableLiveData<Event<Int>>()
    val actionShowToast: LiveData<Event<Int>> get() = _actionShowToast

    private val _actionGoBack = MutableLiveData<Event<Unit>>()
    val actionGoBack: LiveData<Event<Unit>> get() = _actionGoBack

    private val currentState: State get() = _state.value!!

    init {
        _state.value = State(
            userDetailsResult = Result.Empty(),
            deletingInProgress = false
        )
    }

    fun loadUser(userId: Long) {
        if (currentState.userDetailsResult is Result.Success) return
        _state.value = currentState.copy(userDetailsResult = Result.Pending())

        usersService.getById(userId)
            .onSuccess {
                _state.value = currentState.copy(userDetailsResult = Result.Success(it))
            }
            .onError {
                _actionShowToast.value = Event(R.string.cant_load_user_details)
                _actionGoBack.value = Event(Unit)
            }
            .autoCancel()
    }

    fun deleteUser() {
        val userDetailsResult = currentState.userDetailsResult
        if (userDetailsResult !is Result.Success) return
        _state.value = currentState.copy(deletingInProgress = true)
        usersService.deleteUser(userDetailsResult.data.user)
            .onSuccess {
                _actionShowToast.value = Event(R.string.user_has_been_deleted)
                _actionGoBack.value = Event(Unit)
            }
            .onError {
                _state.value = currentState.copy(deletingInProgress = false)
                _actionShowToast.value = Event(R.string.cant_delete_user)
            }
            .autoCancel()
    }
}