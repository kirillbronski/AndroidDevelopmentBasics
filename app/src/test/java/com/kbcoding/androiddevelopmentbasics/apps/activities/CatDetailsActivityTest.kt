package com.kbcoding.androiddevelopmentbasics.apps.activities

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import com.kbcoding.androiddevelopmentbasics.R
import com.kbcoding.androiddevelopmentbasics.di.RepositoriesModule
import com.kbcoding.androiddevelopmentbasics.model.Cat
import com.kbcoding.androiddevelopmentbasics.testUtils.base.BaseRobolectricTest
import com.kbcoding.androiddevelopmentbasics.testUtils.extensions.containsDrawable
import com.kbcoding.androiddevelopmentbasics.testUtils.extensions.with
import com.kbcoding.androiddevelopmentbasics.testUtils.imageLoader.FakeImageLoader
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import io.mockk.every
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@UninstallModules(RepositoriesModule::class)
class CatDetailsActivityTest : BaseRobolectricTest() {

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
        scenario = ActivityScenario.launch(
            CatDetailsActivity::class.java,
            bundleOf(
                CatDetailsActivity.EXTRA_CAT_ID to 1L
            )
        )
        scenario.moveToState(Lifecycle.State.RESUMED)
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun catIsDisplayed() = scenario.with {
        assertEquals(
            "Lucky",
            findViewById<TextView>(R.id.catNameTextView).text
        )
        assertEquals(
            "Meow-meow",
            findViewById<TextView>(R.id.catDescriptionTextView).text
        )
        assertTrue(
            findViewById<ImageView>(R.id.favoriteImageView)
                .containsDrawable(R.drawable.ic_favorite, R.color.highlighted_action)
        )
        assertTrue(
            findViewById<ImageView>(R.id.catImageView)
                .containsDrawable(FakeImageLoader.createDrawable(cat.photoUrl))
        )
    }

    @Test
    fun toggleFavoriteTogglesFlag() = scenario.with {
        // arrange
        every { catsRepository.toggleIsFavorite(any()) } answers {
            val cat = firstArg<Cat>()
            val newCat = cat.copy(isFavorite = !cat.isFavorite)
            catFlow.value = newCat
        }
        val favoriteImageView = findViewById<ImageView>(R.id.favoriteImageView)

        // act 1 - turn off favorite flag
        favoriteImageView.performClick()
        // assert 1
        assertTrue(
            favoriteImageView.containsDrawable(
                R.drawable.ic_favorite_not,
                R.color.action
            )
        )

        // act 2 - turn on favorite flag
        favoriteImageView.performClick()
        // assert 2
        assertTrue(
            favoriteImageView.containsDrawable(
                R.drawable.ic_favorite,
                R.color.highlighted_action
            )
        )
    }

    @Test
    fun clickOnBackFinishesActivity() = scenario.with {
        findViewById<View>(R.id.goBackButton).performClick()
        assertTrue(isFinishing)
    }

}