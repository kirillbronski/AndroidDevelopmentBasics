package com.kbcoding.androiddevelopmentbasics.viewModel

import com.kbcoding.androiddevelopmentbasics.model.Cat
import com.kbcoding.androiddevelopmentbasics.model.CatsRepository
import com.kbcoding.androiddevelopmentbasics.testUtils.base.BaseTest
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CatDetailsViewModelTest : BaseTest() {

    @RelaxedMockK
    lateinit var catsRepository: CatsRepository

    private lateinit var viewModel: CatDetailsViewModel

    private val catId = 1L

    private val cat = Cat(
        id = 1,
        name = "Cat",
        photoUrl = "url",
        description = "desc",
        isFavorite = false
    )
    private val updatedCat = cat.copy(name = "New name")

    private val flow = MutableStateFlow(cat)

    @Before
    fun setUp() {
        every { catsRepository.getCatById(catId) } returns flow
        viewModel = CatDetailsViewModel(catsRepository, catId)
    }

    @Test
    fun toggleFavorite_callsToggleFavorite() {
        viewModel.toggleFavorite()

        verify {
            catsRepository.toggleIsFavorite(cat)
        }
    }

    @Test
    fun init_listensForCat() {
        val cat1 = viewModel.catLiveData.value
        flow.value = updatedCat
        val cat2 = viewModel.catLiveData.value

        assertEquals(cat, cat1)
        assertEquals(updatedCat, cat2)
    }
}