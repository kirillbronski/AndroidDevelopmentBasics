package com.kbcoding.androiddevelopmentbasics.testUtils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kbcoding.androiddevelopmentbasics.model.CatsRepository
import com.kbcoding.androiddevelopmentbasics.testUtils.rules.FakeImageLoaderRule
import com.kbcoding.androiddevelopmentbasics.testUtils.rules.TestViewModelScopeRule
import dagger.hilt.android.testing.HiltAndroidRule
import io.mockk.junit4.MockKRule
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

/**
 * Base class for all UI tests.
 */
open class BaseTest {

    @get:Rule
    val testViewModelScopeRule = TestViewModelScopeRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockkRule = MockKRule(this)

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val fakeImageLoaderRule = FakeImageLoaderRule()

    @Inject
    lateinit var catsRepository: CatsRepository

    @Before
    open fun setUp() {
        hiltRule.inject()
    }

}