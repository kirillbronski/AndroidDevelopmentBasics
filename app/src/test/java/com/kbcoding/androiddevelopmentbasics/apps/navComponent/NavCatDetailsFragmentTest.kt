package com.kbcoding.androiddevelopmentbasics.apps.navComponent

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.test.core.app.ActivityScenario
import com.kbcoding.androiddevelopmentbasics.di.RepositoriesModule
import com.kbcoding.androiddevelopmentbasics.R
import com.kbcoding.androiddevelopmentbasics.model.Cat
import com.kbcoding.androiddevelopmentbasics.testUtils.base.BaseRobolectricTest
import com.kbcoding.androiddevelopmentbasics.testUtils.extensions.containsDrawable
import com.kbcoding.androiddevelopmentbasics.testUtils.extensions.with
import com.kbcoding.androiddevelopmentbasics.testUtils.imageLoader.FakeImageLoader
import com.kbcoding.androiddevelopmentbasics.testUtils.launchNavHiltFragment
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@UninstallModules(RepositoriesModule::class)
class NavCatDetailsFragmentTest : BaseRobolectricTest() {

    @RelaxedMockK
    lateinit var navController: NavController

    private val cat = Cat(
        id = 1,
        name = "Lucky",
        photoUrl = "cat.jpg",
        description = "Meow-meow",
        isFavorite = true
    )

    private val catFlow = MutableStateFlow(cat)

    private lateinit var scenario: ActivityScenario<*>

    @Before
    override fun setUp() {
        super.setUp()
        every { catsRepository.getCatById(any()) } returns catFlow
        val args = NavCatDetailsFragmentArgs(catId = 1)
        scenario = launchNavHiltFragment<NavCatDetailsFragment>(navController, args.toBundle())
    }

    @After
    fun tearDown() {
        scenario.close()
    }


    @Test
    fun catIsDisplayed() = scenario.with {
        Assert.assertEquals(
            "Lucky",
            findViewById<TextView>(R.id.catNameTextView).text
        )
        Assert.assertEquals(
            "Meow-meow",
            findViewById<TextView>(R.id.catDescriptionTextView).text
        )
        Assert.assertTrue(
            findViewById<ImageView>(R.id.favoriteImageView)
                .containsDrawable(R.drawable.ic_favorite, R.color.highlighted_action)
        )
        Assert.assertTrue(
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
        Assert.assertTrue(
            favoriteImageView.containsDrawable(
                R.drawable.ic_favorite_not,
                R.color.action
            )
        )

        // act 2 - turn on favorite flag
        favoriteImageView.performClick()
        // assert 2
        Assert.assertTrue(
            favoriteImageView.containsDrawable(
                R.drawable.ic_favorite,
                R.color.highlighted_action
            )
        )
    }

    @Test
    fun clickOnBackFinishesActivity() = scenario.with {
        findViewById<View>(R.id.goBackButton).performClick()

        verify {
            navController.popBackStack()
        }
    }
}