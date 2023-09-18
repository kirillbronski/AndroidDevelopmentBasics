package com.kbcoding.androiddevelopmentbasics.presentation.users.details

import com.kbcoding.androiddevelopmentbasics.core.result.Result
import com.kbcoding.androiddevelopmentbasics.model.UserDetails

data class State(
    val userDetailsResult: Result<UserDetails>,
    private val deletingInProgress: Boolean
) {
    val showContent: Boolean get() = userDetailsResult is Result.Success
    val showProgress: Boolean get() = userDetailsResult is Result.Pending || deletingInProgress
    val enableDeleteButton: Boolean get() = !deletingInProgress
}
