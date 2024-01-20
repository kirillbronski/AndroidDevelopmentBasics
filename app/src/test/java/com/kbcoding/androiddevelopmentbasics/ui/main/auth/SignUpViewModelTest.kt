package com.kbcoding.androiddevelopmentbasics.ui.main.auth

import com.kbcoding.androiddevelopmentbasics.app.ui.main.auth.SignUpViewModel
import com.kbcoding.androiddevelopmentbasics.app.utils.requireValue
import com.kbcoding.androiddevelopmentbasics.testUtils.CoroutineSubject
import com.kbcoding.androiddevelopmentbasics.testUtils.ViewModelTest
import com.kbcoding.androiddevelopmentbasics.testUtils.returnsSubject
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.just
import io.mockk.runs
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import org.junit.Test
import com.kbcoding.androiddevelopmentbasics.R
import com.kbcoding.androiddevelopmentbasics.app.domain.AccountAlreadyExistsException
import com.kbcoding.androiddevelopmentbasics.app.domain.EmptyFieldException
import com.kbcoding.androiddevelopmentbasics.app.domain.Field
import com.kbcoding.androiddevelopmentbasics.app.domain.PasswordMismatchException
import com.kbcoding.androiddevelopmentbasics.app.domain.accounts.entities.SignUpData
import com.kbcoding.androiddevelopmentbasics.app.ui.main.auth.SignUpViewModel.Companion.NO_ERROR_MESSAGE
import com.kbcoding.androiddevelopmentbasics.core.ViewModelExceptionsTest
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue

class SignUpViewModelTest : ViewModelTest() {

    @InjectMockKs
    lateinit var viewModel: SignUpViewModel

    @Test
    fun testInitialState() {
        val expectedState = createInitialState()

        val state = viewModel.state.requireValue()

        assertEquals(expectedState, state)
    }

    @Test
    fun signUpShowsProgress() {
        val expectedState = createInitialState(
            signUpInProgress = true
        )
        coEvery { accountsRepository.signUp(any()) } returnsSubject CoroutineSubject()

        viewModel.signUp(createSignUpData())

        assertEquals(expectedState, viewModel.state.requireValue())
    }

    @Test
    fun signUpSendsDataToRepository() {
        val expectedData = createSignUpData()
        coEvery { accountsRepository.signUp(any()) } returnsSubject CoroutineSubject()

        viewModel.signUp(expectedData)

        coVerify(exactly = 1) {
            accountsRepository.signUp(expectedData)
        }
    }

    @Test
    fun signUpWithSuccessHidesProgress() {
        coEvery { accountsRepository.signUp(any()) } just runs

        viewModel.signUp(createSignUpData())

        assertFalse(viewModel.state.requireValue().showProgress)
    }

    @Test
    fun signUpWithExceptionHidesProgress() {
        coEvery { accountsRepository.signUp(any()) } throws
                IllegalStateException("Oops")

        viewModel.signUp(createSignUpData())

        assertFalse(viewModel.state.requireValue().showProgress)
    }

    @Test
    fun signUpWithSuccessShowsMessageAndGoesBack() {
        coEvery { accountsRepository.signUp(any()) } just runs

        viewModel.signUp(createSignUpData())

        assertEquals(
            R.string.sign_up_success,
            viewModel.showToastEvent.requireValue().get()
        )
        assertNotNull(viewModel.goBackEvent.requireValue().get())
    }

    @Test
    fun signUpWithEmptyEmailExceptionShowsError() {
        val expectedState = createInitialState(
            emailErrorMessageRes = R.string.field_is_empty
        )
        coEvery { accountsRepository.signUp(any()) } throws
                EmptyFieldException(Field.Email)

        viewModel.signUp(createSignUpData())

        assertEquals(expectedState, viewModel.state.requireValue())
    }

    @Test
    fun signUpWithEmptyUsernameExceptionShowsError() {
        val expectedState = createInitialState(
            usernameErrorMessageRes = R.string.field_is_empty
        )
        coEvery { accountsRepository.signUp(any()) } throws
                EmptyFieldException(Field.Username)

        viewModel.signUp(createSignUpData())

        assertEquals(expectedState, viewModel.state.requireValue())
    }

    @Test
    fun signUpWithEmptyPasswordExceptionShowsError() {
        val expectedState = createInitialState(
            passwordErrorMessageRes = R.string.field_is_empty
        )
        coEvery { accountsRepository.signUp(any()) } throws
                EmptyFieldException(Field.Password)

        viewModel.signUp(createSignUpData())

        assertEquals(expectedState, viewModel.state.requireValue())
    }

    @Test
    fun signUpWithPasswordMismatchExceptionShowsError() {
        val expectedState = createInitialState(
            repeatPasswordErrorMessageRes = R.string.password_mismatch
        )
        coEvery { accountsRepository.signUp(any()) } throws
                PasswordMismatchException()

        viewModel.signUp(createSignUpData())

        assertEquals(expectedState, viewModel.state.requireValue())
    }

    @Test
    fun signUpWithAccountAlreadyExistsExceptionShowsError() {
        val expectedState = createInitialState(
            emailErrorMessageRes = R.string.account_already_exists
        )
        coEvery { accountsRepository.signUp(any()) } throws
                AccountAlreadyExistsException(Exception())

        viewModel.signUp(createSignUpData())

        assertEquals(expectedState, viewModel.state.requireValue())
    }

    @Test
    fun stateShowProgressWithPendingOperationReturnsTrue() {
        val state = createInitialState(signUpInProgress = true)

        val showProgress = state.showProgress

        assertTrue(showProgress)
    }

    @Test
    fun stateEnableViewsWithPendingOperationReturnsFalse() {
        val state = createInitialState(signUpInProgress = true)

        val enableViews = state.enableViews

        assertFalse(enableViews)
    }


    @Test
    fun stateShowProgressWithoutPendingOperationReturnsFalse() {
        val state = createInitialState(signUpInProgress = false)

        val showProgress = state.showProgress

        assertFalse(showProgress)
    }

    @Test
    fun stateEnableViewsWithoutPendingOperationReturnsTrue() {
        val state = createInitialState(signUpInProgress = false)

        val enableViews = state.enableViews

        assertTrue(enableViews)
    }

    class SignUpExceptionsTest : ViewModelExceptionsTest<SignUpViewModel>() {

        @InjectMockKs
        override lateinit var viewModel: SignUpViewModel

        override fun arrangeWithException(e: Exception) {
            coEvery { accountsRepository.signUp(any()) } throws e
        }

        override fun act() {
            viewModel.signUp(createSignUpData())
        }
    }

    private fun createInitialState(
        emailErrorMessageRes: Int = NO_ERROR_MESSAGE,
        passwordErrorMessageRes: Int = NO_ERROR_MESSAGE,
        repeatPasswordErrorMessageRes: Int = NO_ERROR_MESSAGE,
        usernameErrorMessageRes: Int = NO_ERROR_MESSAGE,
        signUpInProgress: Boolean = false
    ) = SignUpViewModel.State(
        emailErrorMessageRes = emailErrorMessageRes,
        passwordErrorMessageRes = passwordErrorMessageRes,
        repeatPasswordErrorMessageRes = repeatPasswordErrorMessageRes,
        usernameErrorMessageRes = usernameErrorMessageRes,
        signUpInProgress = signUpInProgress
    )

    private companion object {
        fun createSignUpData(
            username: String = "username",
            email: String = "email",
            password: String = "password",
            repeatPassword: String = "password"
        ) = SignUpData(
            username = username,
            email = email,
            password = password,
            repeatPassword = repeatPassword
        )
    }

}