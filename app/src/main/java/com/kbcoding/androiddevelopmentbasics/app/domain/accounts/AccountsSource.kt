package com.kbcoding.androiddevelopmentbasics.app.domain.accounts

import com.kbcoding.androiddevelopmentbasics.app.domain.accounts.entities.Account
import com.kbcoding.androiddevelopmentbasics.app.domain.accounts.entities.SignUpData
import kotlinx.coroutines.flow.Flow

interface AccountsSource {

    /**
     * Execute sign-in request.
     * @throws ConnectionException
     * @throws BackendException
     * @throws ParseBackendResponseException
     * @return JWT-token
     */
    suspend fun signIn(email: String, password: String): String

    /**
     * Create a new account.
     * @throws ConnectionException
     * @throws BackendException
     * @throws ParseBackendResponseException
     */
    suspend fun signUp(signUpData: SignUpData)

    /**
     * Get the account info of the current signed-in user.
     * @throws ConnectionException
     * @throws BackendException
     * @throws ParseBackendResponseException
     */
    suspend fun getAccount(): Account

    /**
     * Change the username of the current signed-in user.
     * @throws ConnectionException
     * @throws BackendException
     * @throws ParseBackendResponseException
     */
    suspend fun setUsername(username: String)

}