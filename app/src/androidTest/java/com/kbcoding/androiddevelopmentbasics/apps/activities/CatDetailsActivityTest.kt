package com.kbcoding.androiddevelopmentbasics.apps.activities

import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.kbcoding.androiddevelopmentbasics.R
import com.kbcoding.androiddevelopmentbasics.di.RepositoriesModule
import com.kbcoding.androiddevelopmentbasics.model.Cat
import com.kbcoding.androiddevelopmentbasics.testUtils.BaseTest
import com.kbcoding.androiddevelopmentbasics.testUtils.FakeImageLoader
import com.kbcoding.androiddevelopmentbasics.testUtils.espresso.withDrawable
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.every
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Tests for app with navigation based on activities.
 *
 * This class contains tests for [CatDetailsActivity]. The activity is
 * launched separately by using [ActivityScenario].
 */
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@UninstallModules(RepositoriesModule::class)
@MediumTest
class CatDetailsActivityTest : BaseTest() {

    private lateinit var scenario: ActivityScenario<CatDetailsActivity>

    private val cat = Cat(
        id = 1,
        name = "Lucky",
        photoUrl = "cat.jpg",
        description = "Meow-meow",
        isFavorite = true
    )

    private val catFlow = MutableStateFlow(cat)

    @Before
    override fun setUp() {
        super.setUp()
        every { catsRepository.getCatById(any()) } returns catFlow
        Intents.init()
        scenario = ActivityScenario.launch(
            CatDetailsActivity::class.java,
            bundleOf(
                CatDetailsActivity.EXTRA_CAT_ID to 1L
            )
        )
    }

    @After
    fun tearDown() {
        Intents.release()
        scenario.close()
    }

    @Test
    fun catIsDisplayed() {
        // assert
        onView(withId(R.id.catNameTextView))
            .check(matches(withText("Lucky")))
        onView(withId(R.id.catDescriptionTextView))
            .check(matches(withText("Meow-meow")))
        onView(withId(R.id.favoriteImageView))
            .check(matches(withDrawable(R.drawable.ic_favorite, R.color.highlighted_action)))
        onView(withId(R.id.catImageView))
            .check(matches(withDrawable(FakeImageLoader.createDrawable(cat.photoUrl))))
    }

    @Test
    fun toggleFavoriteTogglesFlag() {
        // arrange
        every { catsRepository.toggleIsFavorite(any()) } answers {
            val cat = firstArg<Cat>()
            val newCat = cat.copy(isFavorite = !cat.isFavorite)
            catFlow.value = newCat
        }

        // act 1 - turn off favorite flag
        onView(withId(R.id.favoriteImageView)).perform(click())
        // assert 1
        onView(withId(R.id.favoriteImageView))
            .check(matches(withDrawable(R.drawable.ic_favorite_not, R.color.action)))

        // act 2 - turn on favorite flag
        onView(withId(R.id.favoriteImageView)).perform(click())
        // assert 2
        onView(withId(R.id.favoriteImageView))
            .check(matches(withDrawable(R.drawable.ic_favorite, R.color.highlighted_action)))
    }

    @Test
    fun clickOnBackFinishesActivity() {
        onView(withId(R.id.goBackButton)).perform(click())
        assertTrue(scenario.state == Lifecycle.State.DESTROYED)
    }

}