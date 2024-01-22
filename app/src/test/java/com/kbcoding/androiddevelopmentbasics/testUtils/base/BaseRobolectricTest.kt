package com.kbcoding.androiddevelopmentbasics.testUtils.base

import com.kbcoding.androiddevelopmentbasics.model.CatsRepository
import com.kbcoding.androiddevelopmentbasics.testUtils.rules.FakeImageLoaderRule
import dagger.hilt.android.testing.HiltAndroidRule
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

open class BaseRobolectricTest : BaseTest() {

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