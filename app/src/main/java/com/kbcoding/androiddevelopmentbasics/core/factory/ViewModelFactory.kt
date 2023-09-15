package com.kbcoding.androiddevelopmentbasics.core.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kbcoding.androiddevelopmentbasics.App
import com.kbcoding.androiddevelopmentbasics.presentation.users.details.UserDetailsViewModel
import com.kbcoding.androiddevelopmentbasics.presentation.users.list.UsersListViewModel

class ViewModelFactory(
    private val app: App
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            UsersListViewModel::class.java -> {
                UsersListViewModel(app.usersService)
            }
            UserDetailsViewModel::class.java -> {
                UserDetailsViewModel(app.usersService)
            }

            else -> {
                throw IllegalStateException("Unknown view model class")
            }
        }
        return viewModel as T
    }
}

