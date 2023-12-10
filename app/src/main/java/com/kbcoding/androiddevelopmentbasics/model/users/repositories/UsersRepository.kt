package com.kbcoding.androiddevelopmentbasics.model.users.repositories

import androidx.paging.PagingData
import com.kbcoding.androiddevelopmentbasics.model.users.User
import kotlinx.coroutines.flow.Flow

interface UsersRepository {

    /**
     * Whether errors are enabled or not. The value is listened by the bottom "Enable Errors" checkbox
     * in the MainActivity.
     */
    fun isErrorsEnabled(): Flow<Boolean>

    /**
     * Enable/disable errors when fetching users.
     */
    fun setErrorsEnabled(value: Boolean)

    /**
     * Get the paging list of users.
     */
    fun getPagedUsers(searchBy: String): Flow<PagingData<User>>

}