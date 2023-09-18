package com.kbcoding.androiddevelopmentbasics.presentation.users.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kbcoding.androiddevelopmentbasics.R
import com.kbcoding.androiddevelopmentbasics.core.presentation.BaseViewModel
import com.kbcoding.androiddevelopmentbasics.core.presentation.Event
import com.kbcoding.androiddevelopmentbasics.core.result.Result
import com.kbcoding.androiddevelopmentbasics.model.User
import com.kbcoding.androiddevelopmentbasics.model.UserListItem
import com.kbcoding.androiddevelopmentbasics.model.UsersListener
import com.kbcoding.androiddevelopmentbasics.model.UsersService

class UsersListViewModel(
    private val usersService: UsersService
) : BaseViewModel(), UserActionListener {

    private val _users = MutableLiveData<Result<List<UserListItem>>>()
    val users: LiveData<Result<List<UserListItem>>> get() = _users

    private val _actionShowDetails = MutableLiveData<Event<User>>()
    val actionShowDetails: LiveData<Event<User>> get() = _actionShowDetails

    private val _actionShowToast = MutableLiveData<Event<Int>>()
    val actionShowToast: LiveData<Event<Int>> get() = _actionShowToast

    private val userIdsInProgress = mutableSetOf<Long>()
    private var usersResult: Result<List<User>> = Result.Empty()
        set(value) {
            field = value
            notifyUpdates()
        }

    private val usersListener: UsersListener = {
        usersResult = if (it.isEmpty()) {
            Result.Empty()
        } else {
            Result.Success(it)
        }
    }

    init {
        usersService.addListener(usersListener)
        loadUsers()
    }

    override fun onCleared() {
        super.onCleared()
        usersService.removeListener(usersListener)
    }

    override fun onUserMove(user: User, moveBy: Int) {
        if (isInProgress(user)) return
        addProgressToItem(user)
        usersService.moveUser(user, moveBy)
            .onSuccess {
                removeProgressFromItem(user)
            }
            .onError {
                removeProgressFromItem(user)
                _actionShowToast.value = Event(R.string.cant_move_user)
            }
            .autoCancel()
    }

    override fun onUserDelete(user: User) {
        if (isInProgress(user)) return
        addProgressToItem(user)
        usersService.deleteUser(user)
            .onSuccess {
                removeProgressFromItem(user)
            }
            .onError {
                removeProgressFromItem(user)
                _actionShowToast.value = Event(R.string.cant_delete_user)
            }
            .autoCancel()
    }

    override fun onUserDetails(user: User) {
        _actionShowDetails.value = Event(user)
    }

    override fun onUserFire(user: User) {
        if (isInProgress(user)) return
        addProgressToItem(user)
        usersService.fireUser(user)
            .onSuccess {
                removeProgressFromItem(user)
            }
            .onError {
                removeProgressFromItem(user)
                _actionShowToast.value = Event(R.string.cant_fire_user)
            }
            .autoCancel()
    }

    fun loadUsers() {
        usersResult = Result.Pending()
        usersService.loadUsers()
            .onError {
                usersResult = Result.Error(it)
            }
            .autoCancel()
    }

//    fun moveUser(user: User, moveBy: Int) {
//        if (isInProgress(user)) return
//        addProgressToItem(user)
//        usersService.moveUser(user, moveBy)
//            .onSuccess {
//                removeProgressFromItem(user)
//            }
//            .onError {
//                removeProgressFromItem(user)
//            }
//            .autoCancel()
//    }

//    fun deleteUser(user: User) {
//        if (isInProgress(user)) return
//        addProgressToItem(user)
//        usersService.deleteUser(user)
//            .onSuccess {
//                removeProgressFromItem(user)
//            }
//            .onError {
//                removeProgressFromItem(user)
//            }
//            .autoCancel()
//    }

//    fun fireUser(user: User) {
//        if (isInProgress(user)) return
//        addProgressToItem(user)
//        usersService.fireUser(user)
//            .onSuccess {
//                removeProgressFromItem(user)
//            }
//            .onError {
//                removeProgressFromItem(user)
//            }
//            .autoCancel()
//    }

//    fun showDetails(user: User) {
//        _actionShowDetails.value = Event(user)
//    }

    private fun addProgressToItem(user: User) {
        userIdsInProgress.add(user.id)
        notifyUpdates()
    }

    private fun removeProgressFromItem(user: User) {
        userIdsInProgress.remove(user.id)
        notifyUpdates()
    }

    private fun isInProgress(user: User): Boolean {
        return userIdsInProgress.contains(user.id)
    }

    private fun notifyUpdates() {
        _users.postValue(usersResult.map { users ->
            users.map { user -> UserListItem(user, isInProgress(user)) }
        })
    }
}
