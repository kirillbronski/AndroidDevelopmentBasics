package com.kbcoding.androiddevelopmentbasics.testUtils.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kbcoding.androiddevelopmentbasics.testUtils.rules.TestDispatcherRule
import io.mockk.junit4.MockKRule
import org.junit.Rule

open class BaseTest {

    @get:Rule
    val testDispatcherRule = TestDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockkRule = MockKRule(this)

}