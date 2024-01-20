package com.kbcoding.androiddevelopmentbasics.testUtils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kbcoding.androiddevelopmentbasics.app.domain.accounts.AccountsRepository
import com.kbcoding.androiddevelopmentbasics.app.utils.logger.Logger
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule

open class ViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val testViewModelScopeRule = TestViewModelScopeRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    lateinit var logger: Logger

    @RelaxedMockK
    lateinit var accountsRepository: AccountsRepository

}