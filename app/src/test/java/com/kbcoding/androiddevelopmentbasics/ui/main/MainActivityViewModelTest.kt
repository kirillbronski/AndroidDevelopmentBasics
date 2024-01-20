package com.kbcoding.androiddevelopmentbasics.ui.main

import com.kbcoding.androiddevelopmentbasics.app.domain.Empty
import com.kbcoding.androiddevelopmentbasics.app.domain.Pending
import com.kbcoding.androiddevelopmentbasics.app.domain.ResponseResult
import com.kbcoding.androiddevelopmentbasics.app.domain.Success
import com.kbcoding.androiddevelopmentbasics.app.domain.accounts.entities.Account
import com.kbcoding.androiddevelopmentbasics.app.ui.main.MainActivityViewModel
import com.kbcoding.androiddevelopmentbasics.testUtils.ViewModelTest
import com.kbcoding.androiddevelopmentbasics.testUtils.createAccount
import io.mockk.every
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import org.junit.Test

class MainActivityViewModelTest : ViewModelTest() {

    @Test
    fun mainViewModelSharesUsernameOfCurrentUser() {
        val account = createAccount(username = "username")
        every { accountsRepository.getAccount() } returns flowOf(Success(account))
        val viewModel = MainActivityViewModel(accountsRepository)

        val username = viewModel.username.value

        assertEquals("@username", username)
    }

    @Test
    fun mainViewModelSharesEmptyStringIfCurrentUserUnavailable() {
        every { accountsRepository.getAccount() } returns flowOf(Empty())
        val viewModel = MainActivityViewModel(accountsRepository)

        val username = viewModel.username.value

        assertEquals("", username)
    }

    @Test
    fun mainViewModelListensForFurtherUsernameUpdates() {
        val flow: MutableStateFlow<ResponseResult<Account>> =
            MutableStateFlow(Success(createAccount(username = "username1")))
        every { accountsRepository.getAccount() } returns flow
        val viewModel = MainActivityViewModel(accountsRepository)

        val username1 = viewModel.username.value
        flow.value = Pending()
        val username2 = viewModel.username.value
        flow.value = Success(createAccount(username = "username2"))
        val username3 = viewModel.username.value

        assertEquals("@username1", username1)
        assertEquals("", username2)
        assertEquals("@username2", username3)
    }

}