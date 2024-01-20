package com.kbcoding.androiddevelopmentbasics.app.data.accounts.entities

/**
 * Request body for `PUT /me` HTTP-request for updating username of the
 * current logged-in user.
 *
 * JSON example:
 * ```
 * {
 *   "username": "",
 * }
 * ```
 */
data class UpdateUsernameRequestEntity(
    val username: String
)