package com.kbcoding.androiddevelopmentbasics.presentation.users.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kbcoding.androiddevelopmentbasics.model.User
import com.kbcoding.androiddevelopmentbasics.model.UsersListener
import com.kbcoding.androiddevelopmentbasics.model.UsersService

class UsersListViewModel(
    private val usersService: UsersService
) : ViewModel() {

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> get() = _users

    private val usersListener: UsersListener = {
        _users.value = it
    }

    init {
        loadUsers()
    }

    override fun onCleared() {
        super.onCleared()
        usersService.removeListener(usersListener)
    }

    fun loadUsers() {
        usersService.addListener(usersListener)
    }

    fun moveUser(user: User, moveBy: Int) {
        usersService.moveUser(user, moveBy)
    }

    fun deleteUser(user: User) {
        usersService.deleteUser(user)
    }

    fun fireUser(user: User) {
        usersService.fireUser(user)
    }
}