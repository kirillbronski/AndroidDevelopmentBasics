package com.kbcoding.androiddevelopmentbasics.ui.main.tabs.profile
import com.kbcoding.androiddevelopmentbasics.app.domain.Pending
import com.kbcoding.androiddevelopmentbasics.app.domain.ResponseResult
import com.kbcoding.androiddevelopmentbasics.app.domain.Success
import com.kbcoding.androiddevelopmentbasics.app.domain.accounts.entities.Account
import com.kbcoding.androiddevelopmentbasics.app.ui.main.tabs.profile.ProfileViewModel
import com.kbcoding.androiddevelopmentbasics.app.utils.requireValue
import com.kbcoding.androiddevelopmentbasics.testUtils.ViewModelTest
import com.kbcoding.androiddevelopmentbasics.testUtils.arranged
import com.kbcoding.androiddevelopmentbasics.testUtils.createAccount
import io.mockk.every
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ProfileViewModelTest : ViewModelTest() {

    private lateinit var flow: MutableStateFlow<ResponseResult<Account>>
    private lateinit var viewModel: ProfileViewModel

    @Before
    fun setUp() {
        flow = MutableStateFlow(Pending())
        every { accountsRepository.getAccount() } returns flow
        viewModel = ProfileViewModel(accountsRepository, logger)
    }

    @Test
    fun reloadReloadsAccount() {
        arranged()

        viewModel.reload()

        verify(exactly = 1) {
            accountsRepository.reloadAccount()
        }
    }

    @Test
    fun accountReturnsDataFromRepository() {
        val expectedAccount1 = Pending<Account>()
        val expectedAccount2 = createAccount(id = 2, username = "name2")
        val expectedAccount3 = createAccount(id = 3, username = "name3")

        flow.value = expectedAccount1
        val result1 = viewModel.account.requireValue()
        flow.value = Success(expectedAccount2)
        val result2 = viewModel.account.requireValue()
        flow.value = Success(expectedAccount3)
        val result3 = viewModel.account.requireValue()

        assertEquals(expectedAccount1, result1)
        assertEquals(expectedAccount2, result2.getValueOrNull())
        assertEquals(expectedAccount3, result3.getValueOrNull())
    }

}