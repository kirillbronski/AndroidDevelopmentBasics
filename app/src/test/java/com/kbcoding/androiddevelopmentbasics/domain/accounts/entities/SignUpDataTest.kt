package com.kbcoding.androiddevelopmentbasics.domain.accounts.entities

import com.kbcoding.androiddevelopmentbasics.app.domain.EmptyFieldException
import com.kbcoding.androiddevelopmentbasics.app.domain.Field
import com.kbcoding.androiddevelopmentbasics.app.domain.PasswordMismatchException
import com.kbcoding.androiddevelopmentbasics.testUtils.catch
import com.kbcoding.androiddevelopmentbasics.testUtils.createSignUpData
import com.kbcoding.androiddevelopmentbasics.testUtils.wellDone
import junit.framework.TestCase.assertEquals
import org.junit.Test

class SignUpDataTest {

    @Test
    fun validateForBlankEmailThrowsException() {
        val signUpData = createSignUpData(email = "     ")

        val exception: EmptyFieldException = catch { signUpData.validate() }

        assertEquals(Field.Email, exception.field)
    }

    @Test
    fun validateForBlankUsernameThrowsException() {
        val signUpData = createSignUpData(username = "     ")

        val exception: EmptyFieldException = catch { signUpData.validate() }

        assertEquals(Field.Username, exception.field)
    }

    @Test
    fun validateForBlankPasswordThrowsException() {
        val signUpData = createSignUpData(password = "     ")

        val exception: EmptyFieldException = catch { signUpData.validate() }

        assertEquals(Field.Password, exception.field)
    }

    @Test
    fun validateForMismatchedPasswordsThrowsException() {
        val signUpData = createSignUpData(
            password = "p1",
            repeatPassword = "p2"
        )

        catch<PasswordMismatchException> { signUpData.validate() }

        wellDone()
    }

    @Test
    fun validateForValidDataDoesNothing() {
        val signUpData = createSignUpData()

        signUpData.validate()

        wellDone()
    }

}