package com.kbcoding.androiddevelopmentbasics.ui.main.tabs.dashboard

import android.graphics.Color
import com.kbcoding.androiddevelopmentbasics.app.domain.Pending
import com.kbcoding.androiddevelopmentbasics.app.domain.ResponseResult
import com.kbcoding.androiddevelopmentbasics.app.domain.Success
import com.kbcoding.androiddevelopmentbasics.app.domain.boxes.BoxesRepository
import com.kbcoding.androiddevelopmentbasics.app.domain.boxes.entities.Box
import com.kbcoding.androiddevelopmentbasics.app.domain.boxes.entities.BoxAndSettings
import com.kbcoding.androiddevelopmentbasics.app.domain.boxes.entities.BoxesFilter
import com.kbcoding.androiddevelopmentbasics.app.ui.main.tabs.dashboard.DashboardViewModel
import com.kbcoding.androiddevelopmentbasics.app.utils.requireValue
import com.kbcoding.androiddevelopmentbasics.testUtils.ViewModelTest
import com.kbcoding.androiddevelopmentbasics.testUtils.createBoxAndSettings
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Test

class DashboardViewModelTest : ViewModelTest() {

    @MockK
    lateinit var boxesRepository: BoxesRepository

    private lateinit var boxesFlow: MutableStateFlow<ResponseResult<List<BoxAndSettings>>>
    private lateinit var viewModel: DashboardViewModel

    @Before
    fun setUp() {
        boxesFlow = MutableStateFlow(Pending())
        every { boxesRepository.getBoxesAndSettings(BoxesFilter.ONLY_ACTIVE) } returns boxesFlow
        viewModel = DashboardViewModel(boxesRepository, accountsRepository, logger)
    }

    @Test
    fun reloadReloadsOnlyActiveData() {
        every { boxesRepository.reload(any()) } just runs

        viewModel.reload()

        verify(exactly = 1) {
            boxesRepository.reload(BoxesFilter.ONLY_ACTIVE)
        }
    }

    @Test
    fun boxesReturnsDataFromRepository() {
        val expectedList1 = listOf(
            createBoxAndSettings(id = 2, name = "Red", value = Color.RED),
            createBoxAndSettings(id = 3, name = "Green", value = Color.GREEN)
        )
        val expectedList2 = listOf(
            createBoxAndSettings(id = 3, name = "Green", value = Color.GREEN),
            createBoxAndSettings(id = 4, name = "Blue", value = Color.BLUE)
        )

        boxesFlow.value = Pending()
        val result1 = viewModel.boxes.requireValue()
        boxesFlow.value = Success(expectedList1)
        val result2 = viewModel.boxes.requireValue()
        boxesFlow.value = Success(expectedList2)
        val result3 = viewModel.boxes.requireValue()

        assertEquals(Pending<List<Box>>(), result1)
        assertEquals(expectedList1.map { it.box }, result2.getValueOrNull())
        assertEquals(expectedList2.map { it.box }, result3.getValueOrNull())
        verify(exactly = 1) {
            boxesRepository.getBoxesAndSettings(BoxesFilter.ONLY_ACTIVE)
        }
    }

}