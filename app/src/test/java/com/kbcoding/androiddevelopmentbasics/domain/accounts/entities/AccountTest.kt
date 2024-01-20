package com.kbcoding.androiddevelopmentbasics.domain.accounts.entities

import com.kbcoding.androiddevelopmentbasics.app.domain.accounts.entities.Account
import junit.framework.TestCase.assertEquals
import org.junit.Test

class AccountTest {

    @Test
    fun newInstanceUsesUnknownCreatedAtValue() {
        val account = Account(
            id = 1,
            username = "username",
            email = "email"
        )

        val createdAt = account.createdAt

        assertEquals(Account.UNKNOWN_CREATED_AT, createdAt)
    }
}