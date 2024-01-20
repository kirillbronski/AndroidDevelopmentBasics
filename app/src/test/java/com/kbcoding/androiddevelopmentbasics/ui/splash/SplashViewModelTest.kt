package com.kbcoding.androiddevelopmentbasics.ui.splash

import com.kbcoding.androiddevelopmentbasics.app.domain.accounts.AccountsRepository
import com.kbcoding.androiddevelopmentbasics.app.ui.splash.SplashViewModel
import com.kbcoding.androiddevelopmentbasics.app.utils.requireValue
import com.kbcoding.androiddevelopmentbasics.testUtils.ViewModelTest
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class SplashViewModelTest : ViewModelTest() {

    @Test
    fun splashViewModelWithSignedInUserSendsLaunchMainScreenWithTrueValue() {
        val accountsRepository = mockk<AccountsRepository>()
        every { accountsRepository.isSignedIn() } returns true

        val viewModel = SplashViewModel(accountsRepository)

        val isSignedIn = viewModel.launchMainScreenEvent.requireValue().get()!!
        assertTrue(isSignedIn)
    }

    @Test
    fun splashViewModelWithoutSignedInUserSendsLaunchMainScreenWithFalseValue() {
        val accountsRepository = mockk<AccountsRepository>()
        every { accountsRepository.isSignedIn() } returns false

        val viewModel = SplashViewModel(accountsRepository)

        val isSignedIn = viewModel.launchMainScreenEvent.requireValue().get()!!
        assertFalse(isSignedIn)
    }
}