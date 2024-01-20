package com.kbcoding.androiddevelopmentbasics.ui.main.auth

import com.kbcoding.androiddevelopmentbasics.R
import com.kbcoding.androiddevelopmentbasics.app.domain.EmptyFieldException
import com.kbcoding.androiddevelopmentbasics.app.domain.Field
import com.kbcoding.androiddevelopmentbasics.app.domain.InvalidCredentialsException
import com.kbcoding.androiddevelopmentbasics.app.ui.main.auth.SignInViewModel
import com.kbcoding.androiddevelopmentbasics.app.utils.requireValue
import com.kbcoding.androiddevelopmentbasics.core.ViewModelExceptionsTest
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
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import org.junit.Test

class SignInViewModelTest : ViewModelTest() {

    @InjectMockKs
    lateinit var viewModel: SignInViewModel

    @Test
    fun testInitialState() {
        val expectedState = SignInViewModel.State(
            emptyEmailError = false,
            emptyPasswordError = false,
            signInInProgress = false
        )

        val state = viewModel.state.requireValue()

        assertEquals(expectedState, state)
    }

    @Test
    fun signInShowsProgress() {
        coEvery { accountsRepository.signIn(any(), any()) } returnsSubject
                CoroutineSubject()

        viewModel.signIn("email", "password")

        assertTrue(viewModel.state.requireValue().showProgress)
    }

    @Test
    fun signInSendsCredentialsToRepository() {
        coEvery { accountsRepository.signIn(any(), any()) } returnsSubject
                CoroutineSubject()

        viewModel.signIn("email", "password")

        coVerify(exactly = 1) {
            accountsRepository.signIn("email", "password")
        }
    }

    @Test
    fun signInWithSuccessHidesProgress() {
        val subject = CoroutineSubject<Unit>()
        coEvery { accountsRepository.signIn(any(), any()) } returnsSubject subject

        viewModel.signIn("email", "password")

        assertTrue(viewModel.state.requireValue().showProgress)
        subject.sendSuccess(Unit)
        assertFalse(viewModel.state.requireValue().showProgress)
    }

    @Test
    fun signInWithExceptionHidesProgress() {
        coEvery { accountsRepository.signIn(any(), any()) } throws
                IllegalStateException("Oops")

        viewModel.signIn("email", "password")

        assertFalse(viewModel.state.requireValue().showProgress)
    }

    @Test
    fun signInWithEmptyEmailExceptionShowsError() {
        val expectedState = SignInViewModel.State(
            emptyPasswordError = false,
            emptyEmailError = true,
            signInInProgress = false
        )
        coEvery { accountsRepository.signIn(any(), any()) } throws
                EmptyFieldException(Field.Email)

        viewModel.signIn("email", "password")

        assertEquals(expectedState, viewModel.state.requireValue())
    }

    @Test
    fun signInWithEmptyPasswordExceptionShowsError() {
        val expectedState = SignInViewModel.State(
            emptyPasswordError = true,
            emptyEmailError = false,
            signInInProgress = false
        )
        coEvery { accountsRepository.signIn(any(), any()) } throws
                EmptyFieldException(Field.Password)

        viewModel.signIn("email", "password")

        assertEquals(expectedState, viewModel.state.requireValue())
    }

    @Test
    fun signInWithInvalidCredentialsExceptionShowsErrorAndClearsPasswordField() {
        val expectedState = SignInViewModel.State(
            emptyPasswordError = false,
            emptyEmailError = false,
            signInInProgress = false
        )
        coEvery { accountsRepository.signIn(any(), any()) } throws
                InvalidCredentialsException(Exception())

        viewModel.signIn("email", "password")

        assertNotNull(viewModel.clearPasswordEvent.requireValue().get())
        assertEquals(
            R.string.invalid_email_or_password,
            viewModel.showAuthToastEvent.requireValue().get()
        )
        assertEquals(expectedState, viewModel.state.requireValue())
    }

    @Test
    fun signInWithSuccessLaunchesTabsScreen() {
        coEvery { accountsRepository.signIn(any(), any()) } just runs

        viewModel.signIn("email", "password")

        assertNotNull(viewModel.navigateToTabsEvent.requireValue().get())
    }

    class SignInExceptionsTest : ViewModelExceptionsTest<SignInViewModel>() {
        @InjectMockKs
        override lateinit var viewModel: SignInViewModel

        override fun arrangeWithException(e: Exception) {
            coEvery { accountsRepository.signIn(any(), any()) } throws e
        }

        override fun act() {
            viewModel.signIn("email", "password")
        }
    }

}