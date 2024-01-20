package com.kbcoding.androiddevelopmentbasics.data.accounts.entities

import com.kbcoding.androiddevelopmentbasics.app.data.accounts.entities.GetAccountResponseEntity
import com.kbcoding.androiddevelopmentbasics.app.domain.accounts.entities.Account
import junit.framework.TestCase.assertEquals
import org.junit.Test

class GetAccountResponseEntityTest {

    @Test
    fun toAccountMapsToInAppEntity() {
        val responseEntity = GetAccountResponseEntity(
            id = 3,
            email = "some-email",
            username = "some-username",
            createdAt = 123
        )

        val inAppEntity = responseEntity.toAccount()

        val expectedInAppEntity = Account(
            id = 3,
            email = "some-email",
            username = "some-username",
            createdAt = 123
        )
        assertEquals(expectedInAppEntity, inAppEntity)
    }
}