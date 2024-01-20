package com.kbcoding.androiddevelopmentbasics.ui.main.tabs.profile

import com.kbcoding.androiddevelopmentbasics.R
import com.kbcoding.androiddevelopmentbasics.app.domain.EmptyFieldException
import com.kbcoding.androiddevelopmentbasics.app.domain.Field
import com.kbcoding.androiddevelopmentbasics.app.domain.Pending
import com.kbcoding.androiddevelopmentbasics.app.domain.ResponseResult
import com.kbcoding.androiddevelopmentbasics.app.domain.Success
import com.kbcoding.androiddevelopmentbasics.app.domain.accounts.entities.Account
import com.kbcoding.androiddevelopmentbasics.app.ui.main.tabs.profile.EditProfileViewModel
import com.kbcoding.androiddevelopmentbasics.app.utils.requireValue
import com.kbcoding.androiddevelopmentbasics.core.ViewModelExceptionsTest
import com.kbcoding.androiddevelopmentbasics.testUtils.CoroutineSubject
import com.kbcoding.androiddevelopmentbasics.testUtils.ViewModelTest
import com.kbcoding.androiddevelopmentbasics.testUtils.arranged
import com.kbcoding.androiddevelopmentbasics.testUtils.createAccount
import com.kbcoding.androiddevelopmentbasics.testUtils.returnsSubject
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class EditProfileViewModelTest : ViewModelTest() {

    private lateinit var flow: MutableStateFlow<ResponseResult<Account>>

    private lateinit var viewModel: EditProfileViewModel

    @Before
    fun setUp() {
        flow = MutableStateFlow(Pending())
        every { accountsRepository.getAccount() } returns flow
        viewModel = EditProfileViewModel(accountsRepository, logger)
    }

    @Test
    fun saveUsernameShowsProgress() {
        coEvery { accountsRepository.updateAccountUsername(any()) } returnsSubject
                CoroutineSubject()

        viewModel.saveUsername("username")

        assertTrue(viewModel.saveInProgress.requireValue())
    }

    @Test
    fun saveUsernameSendsUsernameToRepository() {
        coEvery { accountsRepository.updateAccountUsername(any()) } returnsSubject
                CoroutineSubject()

        viewModel.saveUsername("username")

        coVerify(exactly = 1) {
            accountsRepository.updateAccountUsername("username")
        }
    }

    @Test
    fun saveUsernameWithSuccessHidesProgressAndGoesBack() {
        coEvery { accountsRepository.updateAccountUsername(any()) } just runs

        viewModel.saveUsername("username")

        assertFalse(viewModel.saveInProgress.requireValue())
        assertNotNull(viewModel.goBackEvent.requireValue().get())
    }

    @Test
    fun saveUsernameWithErrorHidesProgress() {
        coEvery { accountsRepository.updateAccountUsername(any()) } throws
                IllegalStateException()

        viewModel.saveUsername("username")

        assertFalse(viewModel.saveInProgress.requireValue())
        assertNull(viewModel.goBackEvent.value?.get())
    }

    @Test
    fun saveUsernameWithEmptyValueShowsError() {
        coEvery { accountsRepository.updateAccountUsername(any()) } throws
                EmptyFieldException(Field.Username)

        viewModel.saveUsername("username")

        assertEquals(
            R.string.field_is_empty,
            viewModel.showErrorMessageResEvent.requireValue().get()
        )
    }

    @Test
    fun initialUsernameEventReturnsFirstValueFromRepository() {
        arranged()

        flow.value = Success(createAccount(username = "username1"))
        val value1 = viewModel.initialUsernameEvent.value?.get()
        flow.value = Success(createAccount(username = "username2"))
        val value2 = viewModel.initialUsernameEvent.value?.get()

        assertEquals("username1", value1)
        assertNull(value2)
    }

    class SaveUsernameExceptionsTest : ViewModelExceptionsTest<EditProfileViewModel>() {

        override lateinit var viewModel: EditProfileViewModel

        @Before
        fun setUp() {
            every { accountsRepository.getAccount() } returns
                    flowOf(Success(createAccount()))
            viewModel = EditProfileViewModel(accountsRepository, logger)
        }

        override fun arrangeWithException(e: Exception) {
            coEvery { accountsRepository.updateAccountUsername(any()) } throws e
        }

        override fun act() {
            viewModel.saveUsername("username")
        }
    }

}