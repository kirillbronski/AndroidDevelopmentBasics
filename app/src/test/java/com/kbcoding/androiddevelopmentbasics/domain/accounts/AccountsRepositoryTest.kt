package com.kbcoding.androiddevelopmentbasics.domain.accounts

import com.kbcoding.androiddevelopmentbasics.app.domain.AccountAlreadyExistsException
import com.kbcoding.androiddevelopmentbasics.app.domain.AuthException
import com.kbcoding.androiddevelopmentbasics.app.domain.BackendException
import com.kbcoding.androiddevelopmentbasics.app.domain.EmptyFieldException
import com.kbcoding.androiddevelopmentbasics.app.domain.Field
import com.kbcoding.androiddevelopmentbasics.app.domain.InvalidCredentialsException
import com.kbcoding.androiddevelopmentbasics.app.domain.ResponseResult
import com.kbcoding.androiddevelopmentbasics.app.domain.accounts.AccountsRepository
import com.kbcoding.androiddevelopmentbasics.app.domain.accounts.AccountsSource
import com.kbcoding.androiddevelopmentbasics.app.domain.accounts.entities.Account
import com.kbcoding.androiddevelopmentbasics.app.domain.accounts.entities.SignUpData
import com.kbcoding.androiddevelopmentbasics.app.domain.settings.AppSettings
import com.kbcoding.androiddevelopmentbasics.app.domain.wrapBackendExceptions
import com.kbcoding.androiddevelopmentbasics.app.utils.async.LazyFlowFactory
import com.kbcoding.androiddevelopmentbasics.app.utils.async.LazyFlowSubject
import com.kbcoding.androiddevelopmentbasics.app.utils.async.SuspendValueLoader
import com.kbcoding.androiddevelopmentbasics.testUtils.arranged
import com.kbcoding.androiddevelopmentbasics.testUtils.catch
import com.kbcoding.androiddevelopmentbasics.testUtils.createAccount
import com.kbcoding.androiddevelopmentbasics.testUtils.wellDone
import io.mockk.CapturingSlot
import io.mockk.called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkStatic
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertSame
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AccountsRepositoryTest {

    @get:Rule
    val rule = MockKRule(this)

    @RelaxedMockK
    lateinit var accountsSource: AccountsSource

    @RelaxedMockK
    lateinit var appSettings: AppSettings

    @RelaxedMockK
    lateinit var lazyFlowFactory: LazyFlowFactory

    @RelaxedMockK
    lateinit var lazyFlowSubject: LazyFlowSubject<Unit, Account>

    private lateinit var accountsRepository: AccountsRepository

    @Before
    fun setUp() {
        every {
            lazyFlowFactory.createLazyFlowSubject<Unit, Account>(any())
        } returns lazyFlowSubject

        accountsRepository = createRepository()

        mockkStatic("com.kbcoding.androiddevelopmentbasics.app.domain.ExceptionsKt")
    }

    @After
    fun tearDown() {
        unmockkStatic("com.kbcoding.androiddevelopmentbasics.app.domain.ExceptionsKt")
    }

    @Test
    fun isSignedInWithSavedTokenReturnsTrue() {
        every { appSettings.getCurrentToken() } returns "some-token"

        val isSignedIn = accountsRepository.isSignedIn()

        assertTrue(isSignedIn)
    }

    @Test
    fun isSignedInWithoutSavedTokenReturnsFalse() {
        every { appSettings.getCurrentToken() } returns null

        val isSignedIn = accountsRepository.isSignedIn()

        assertFalse(isSignedIn)
    }

    @Test
    fun signInWithEmptyEmailThrowsException()  = runTest {
        arranged()

        val exception: EmptyFieldException = catch {
            accountsRepository.signIn("   ", "123")
        }

        assertEquals(Field.Email, exception.field)
        verify {
            accountsSource wasNot called
        }
    }

    @Test
    fun signInWithEmptyPasswordThrowsException()  = runTest {
        arranged()

        val exception: EmptyFieldException = catch {
            accountsRepository.signIn("email", "   ")
        }

        assertEquals(Field.Password, exception.field)
        verify {
            accountsSource wasNot called
        }
    }

    @Test
    fun signInInvokesSourceWithSameEmailAndPasswordArgs() = runTest {
        val expectedEmail = "email"
        val expectedPassword = "password"

        accountsRepository.signIn(expectedEmail, expectedPassword)

        coVerify {
            accountsSource.signIn(expectedEmail, expectedPassword)
        }
    }

    @Test
    fun signInWithFailedOperationRethrowsException() = runTest {
        val expectedException = IllegalStateException("Some exception")
        coEvery { accountsSource.signIn(any(), any()) } throws expectedException

        val exception: IllegalStateException = catch {
            accountsRepository.signIn("email", "password")
        }

        assertSame(expectedException, exception)
    }

    @Test
    fun signInWithBackend401ExceptionThrowsInvalidCredentialsException() = runTest {
        coEvery { accountsSource.signIn(any(), any()) } throws BackendException(
            code = 401,
            message = "Auth error"
        )

        catch<InvalidCredentialsException> {
            accountsRepository.signIn("email", "password")
        }

        wellDone()
    }

    @Test
    fun signInWithSuccessfulOperationSavesToken() = runTest {
        val expectedToken = "some-token"
        coEvery { accountsSource.signIn(any(), any()) } returns expectedToken

        accountsRepository.signIn("email", "password")

        verify(exactly = 1) {
            appSettings.setCurrentToken(expectedToken)
        }
    }

    @Test
    fun signInWithSuccessfulOperationNotifiesAboutSignedInAccount()  = runTest {
        val expectedAccount = createAccount()
        coEvery { accountsSource.signIn(any(), any()) } returns "some-token"
        coEvery { accountsSource.getAccount() } returns expectedAccount

        accountsRepository.signIn("email", "password")

        coVerify(exactly = 1) {
            lazyFlowSubject.updateAllValues(refEq(expectedAccount))
        }
    }

    @Test
    fun signUpWithInvalidDataThrowsException() = runTest {
        val expectedException = IllegalStateException("Some exception")
        val signUpData = mockk<SignUpData>()
        every { signUpData.validate() } throws expectedException

        val exception: IllegalStateException = catch {
            accountsRepository.signUp(signUpData)
        }

        assertSame(expectedException, exception)
        coVerify {
            accountsSource wasNot called
        }
    }

    @Test
    fun signUpInvokesSourceWithSameArgs() = runTest {
        val signUpData = mockk<SignUpData>(relaxed = true)

        accountsRepository.signUp(signUpData)

        coVerify {
            accountsSource.signUp(refEq(signUpData))
        }
    }

    @Test
    fun signUpWithFailedOperationRethrowsException() = runTest {
        val signUpData = mockk<SignUpData>(relaxed = true)
        val expectedException = IllegalStateException("Oops")
        coEvery { accountsSource.signUp(any()) } throws expectedException

        val exception: IllegalStateException = catch {
            accountsRepository.signUp(signUpData)
        }

        assertSame(expectedException, exception)
    }

    @Test
    fun signUpWithBackend409ExceptionThrowsAccountAlreadyExistsException() = runTest {
        val signUpData = mockk<SignUpData>(relaxed = true)
        val expectedException = BackendException(
            code = 409,
            message = "Already exists"
        )
        coEvery { accountsSource.signUp(any()) } throws expectedException

        catch<AccountAlreadyExistsException> {
            accountsRepository.signUp(signUpData)
        }

        wellDone()
    }

    @Test
    fun signUpWithOtherBackendExceptionRethrowsIt() = runTest {
        val signUpData = mockk<SignUpData>(relaxed = true)
        val expectedException = BackendException(
            code = 400,
            message = "Some backend error message"
        )
        coEvery { accountsSource.signUp(any()) } throws expectedException

        val exception: BackendException = catch {
            accountsRepository.signUp(signUpData)
        }

        assertSame(expectedException, exception)
    }

    @Test
    fun reloadAccountTriggersFlowReloading() {
        arranged()

        accountsRepository.reloadAccount()

        verify(exactly = 1) {
            lazyFlowSubject.reloadAll()
        }
    }

    @Test
    fun getAccountReturnsLazySubjectFlow() {
        val expectedFlow = mockk<Flow<ResponseResult<Account>>>()
        every { lazyFlowSubject.listen(Unit) } returns expectedFlow

        val flow = accountsRepository.getAccount()

        assertSame(expectedFlow, flow)
    }

    @Test
    fun updateAccountUsernameUpdatesUsernameAndNotifiesAccountFlow() = runTest {
        val expectedNewUsername = "newUsername"
        val expectedNewAccount = createAccount(username = expectedNewUsername)
        coEvery { accountsSource.getAccount() } returns expectedNewAccount

        accountsRepository.updateAccountUsername(expectedNewUsername)

        coVerifyOrder {
            accountsSource.setUsername(expectedNewUsername)
            lazyFlowSubject.updateAllValues(refEq(expectedNewAccount))
        }
    }

    @Test
    fun logoutClearsTokenAndResetsAccountFlow() {
        arranged()

        accountsRepository.logout()

        verify {
            appSettings.setCurrentToken(null)
            lazyFlowSubject.updateAllValues(null)
        }
    }

    @Test
    fun updateAccountUsernameWithEmptyUsernameThrowsException() = runTest {
        arranged()

        val exception: EmptyFieldException = catch {
            accountsRepository.updateAccountUsername("  ")
        }

        assertEquals(Field.Username, exception.field)
    }

    @Test
    fun updateAccountUsernameWithFailedUpdateOperationRethrowsException() = runTest {
        val expectedException = IllegalStateException("Oops")
        coEvery { accountsSource.setUsername(any()) } throws expectedException

        val exception: IllegalStateException = catch {
            accountsRepository.updateAccountUsername("username")
        }

        assertSame(expectedException, exception)
    }

    @Test
    fun updateAccountUsernameWithFailedFetchAccountOperationRethrowsException() = runTest {
        val expectedException = IllegalStateException("Oops again")
        coEvery { accountsSource.getAccount() } throws expectedException

        val exception: IllegalStateException = catch {
            accountsRepository.updateAccountUsername("username")
        }

        assertSame(expectedException, exception)
    }

    @Test
    fun updateAccountUsernameWithBackend401ExceptionOnUpdateOperationThrowsAuthException() = runTest {
        coEvery { accountsSource.setUsername(any()) } throws BackendException(
            code = 401,
            message = "Auth error"
        )

        catch<AuthException> {
            accountsRepository.updateAccountUsername("username")
        }

        wellDone()
    }

    @Test
    fun updateAccountUsernameWithBackend401ExceptionOnFetchAccountOperationThrowsAuthException() = runTest {
        coEvery { accountsSource.getAccount() } throws BackendException(
            code = 401,
            message = "Some auth error"
        )

        catch<AuthException> {
            accountsRepository.updateAccountUsername("username")
        }

        wellDone()
    }

    @Test
    fun initLoadsCurrentAccount() = runTest {
        val expectedAccount = createAccount(id = 123)
        coEvery { accountsSource.getAccount() } returns expectedAccount
        val slot =
            arrangeRepositoryWithLazyFlowSlot()

        val account = slot.captured.invoke(Unit)

        assertSame(expectedAccount, account)
    }

    @Test
    fun initWith404BackendExceptionThrowsAuthException() = runTest {
        coEvery { accountsSource.getAccount() } throws
                BackendException(code = 404, message = "")
        val slot =
            arrangeRepositoryWithLazyFlowSlot()

        catch<AuthException> {
            slot.captured.invoke(Unit)
        }

        wellDone()
    }

    @Test
    fun initWithOtherBackendExceptionRethrowsIt() = runTest {
        val expectedException = BackendException(code = 400, message = "123")
        coEvery { accountsSource.getAccount() } throws expectedException
        val slot =
            arrangeRepositoryWithLazyFlowSlot()

        val exception: BackendException = catch {
            slot.captured.invoke(Unit)
        }

        assertSame(expectedException, exception)
    }

    @Test
    fun initWithOtherExceptionRethrowsException() = runTest {
        val expectedException = IllegalStateException("Oops")
        coEvery { accountsSource.getAccount() } throws expectedException
        val slot =
            arrangeRepositoryWithLazyFlowSlot()

        val exception = catch<IllegalStateException> {
            slot.captured.invoke(Unit)
        }

        assertSame(expectedException, exception)
    }

    @Test
    fun initUsesDefaultBackendExceptionsWrapper() = runTest {
        val expectedAccount = createAccount(id = 321)
        coEvery { wrapBackendExceptions<Account>(any()) } returns expectedAccount
        val slot =
            arrangeRepositoryWithLazyFlowSlot()

        val account = slot.captured.invoke(Unit)

        assertSame(expectedAccount, account)
        coVerify {
            accountsSource wasNot called
        }
    }

    private fun arrangeRepositoryWithLazyFlowSlot(): CapturingSlot<SuspendValueLoader<Unit, Account>> {
        val factory: LazyFlowFactory = mockk()
        val slot: CapturingSlot<SuspendValueLoader<Unit, Account>> = slot()
        every { factory.createLazyFlowSubject(capture(slot)) } returns mockk(relaxed = true)

        createRepository(lazyFlowFactory = factory)

        return slot
    }

    private fun createRepository(
        lazyFlowFactory: LazyFlowFactory = this.lazyFlowFactory
    ): AccountsRepository = AccountsRepository(
        accountsSource = this.accountsSource,
        appSettings = this.appSettings,
        lazyFlowFactory = lazyFlowFactory
    )

}