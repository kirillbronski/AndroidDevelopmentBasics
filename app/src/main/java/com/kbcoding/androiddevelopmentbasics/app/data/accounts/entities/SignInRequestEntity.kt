package com.kbcoding.androiddevelopmentbasics.app.data.accounts.entities

/**
 * Request body for `POST /sign-in` HTTP-request.
 *
 * JSON example:
 * ```
 * {
 *   "email": "",
 *   "password": "",
 * }
 * ```
 */
data class SignInRequestEntity(
    val email: String,
    val password: String
)