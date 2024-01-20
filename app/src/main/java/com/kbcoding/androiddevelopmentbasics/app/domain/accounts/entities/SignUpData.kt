package com.kbcoding.androiddevelopmentbasics.app.domain.accounts.entities

import com.kbcoding.androiddevelopmentbasics.app.domain.EmptyFieldException
import com.kbcoding.androiddevelopmentbasics.app.domain.Field
import com.kbcoding.androiddevelopmentbasics.app.domain.PasswordMismatchException

/**
 * Fields that should be provided during creating a new account.
 */
data class SignUpData(
    val username: String,
    val email: String,
    val password: String,
    val repeatPassword: String
) {

    /**
     * @throws EmptyFieldException
     * @throws PasswordMismatchException
     */
    fun validate() {
        if (email.isBlank()) throw EmptyFieldException(Field.Email)
        if (username.isBlank()) throw EmptyFieldException(Field.Username)
        if (password.isBlank()) throw EmptyFieldException(Field.Password)
        if (password != repeatPassword) throw PasswordMismatchException()
    }
}