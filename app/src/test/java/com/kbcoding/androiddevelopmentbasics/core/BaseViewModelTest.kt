package com.kbcoding.androiddevelopmentbasics.core

import com.kbcoding.androiddevelopmentbasics.testUtils.ViewModelTest
import com.kbcoding.androiddevelopmentbasics.testUtils.arranged
import com.kbcoding.core.BaseViewModel
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.verify
import org.junit.Test

class BaseViewModelTest : ViewModelTest() {

    @InjectMockKs
    lateinit var viewModel: BaseViewModel

    @Test
    fun logoutCallsLogout() {
        arranged()

        viewModel.logout()

        verify(exactly = 1) {
            accountsRepository.logout()
        }
    }

    @Test
    fun logErrorLogsError() {
        val exception = IllegalStateException()

        viewModel.logError(exception)

        verify(exactly = 1) {
            logger.error(any(), refEq(exception))
        }
    }

}