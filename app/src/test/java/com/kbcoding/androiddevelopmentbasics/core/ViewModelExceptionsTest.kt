package com.kbcoding.androiddevelopmentbasics.core

import com.kbcoding.androiddevelopmentbasics.R
import com.kbcoding.androiddevelopmentbasics.app.domain.AuthException
import com.kbcoding.androiddevelopmentbasics.app.domain.BackendException
import com.kbcoding.androiddevelopmentbasics.app.domain.ConnectionException
import com.kbcoding.androiddevelopmentbasics.app.utils.requireValue
import com.kbcoding.androiddevelopmentbasics.testUtils.ViewModelTest
import com.kbcoding.core.BaseViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Test

abstract class ViewModelExceptionsTest<VM : BaseViewModel> : ViewModelTest() {

    abstract val viewModel: VM

    abstract fun arrangeWithException(e: Exception)

    abstract fun act()

    @Test
    fun safeLaunchWithConnectionExceptionShowsMessage() {
        val exception = ConnectionException(Exception())
        arrangeWithException(exception)

        act()

        assertEquals(
            R.string.connection_error,
            viewModel.showErrorMessageResEvent.requireValue().get()
        )
    }

    @Test
    fun safeLaunchWithBackendExceptionShowsMessage() {
        val exception = BackendException(404, "Some error message")
        arrangeWithException(exception)

        act()

        assertEquals(
            exception.message,
            viewModel.showErrorMessageEvent.requireValue().get()
        )
    }

    @Test
    fun safeLaunchWithAuthExceptionRestartsFromLoginScreen() {
        val exception = AuthException(Exception())
        arrangeWithException(exception)

        act()

        assertNotNull(
            viewModel.showAuthErrorAndRestartEvent.requireValue().get()
        )
    }

    @Test
    fun safeLaunchWithOtherExceptionsShowsInternalErrorMessage() {
        val exception = IllegalStateException()
        arrangeWithException(exception)

        act()

        assertEquals(
            R.string.internal_error,
            viewModel.showErrorMessageResEvent.requireValue().get()
        )
    }

}