package com.kbcoding.androiddevelopmentbasics.domain

import com.kbcoding.androiddevelopmentbasics.app.domain.AuthException
import com.kbcoding.androiddevelopmentbasics.app.domain.BackendException
import com.kbcoding.androiddevelopmentbasics.app.domain.wrapBackendExceptions
import com.kbcoding.androiddevelopmentbasics.testUtils.catch
import com.kbcoding.androiddevelopmentbasics.testUtils.wellDone
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ExceptionsTest {

    lateinit var block: suspend () -> String

    @Before
    fun setUp() {
        block = mockk()
    }

    @Test
    fun wrapBackendExceptionsRethrowsOtherExceptions() = runTest {
        val expectedException = IllegalStateException()
        coEvery { block() } throws expectedException

        val exception: IllegalStateException = catch {
            wrapBackendExceptions(block)
        }

        assertSame(expectedException, exception)
    }

    @Test
    fun wrapBackendExceptionsMaps401ErrorToAuthException() = runTest {
        coEvery { block() } throws BackendException(
            code = 401,
            message = "Oops"
        )

        catch<AuthException> { wrapBackendExceptions(block) }

        wellDone()
    }

    @Test
    fun wrapBackendExceptionDoesNotMapOtherBackendExceptions() = runTest {
        val expectedBackendException = BackendException(
            code = 432,
            message = "Boom!"
        )
        coEvery { block() } throws expectedBackendException

        val exception: BackendException = catch {
            wrapBackendExceptions(block)
        }

        assertSame(expectedBackendException, exception)
    }
}