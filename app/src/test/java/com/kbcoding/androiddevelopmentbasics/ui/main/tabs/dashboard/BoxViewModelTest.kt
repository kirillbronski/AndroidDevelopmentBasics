package com.kbcoding.androiddevelopmentbasics.ui.main.tabs.dashboard

import com.kbcoding.androiddevelopmentbasics.app.domain.Pending
import com.kbcoding.androiddevelopmentbasics.app.domain.ResponseResult
import com.kbcoding.androiddevelopmentbasics.app.domain.Success
import com.kbcoding.androiddevelopmentbasics.app.domain.boxes.BoxesRepository
import com.kbcoding.androiddevelopmentbasics.app.domain.boxes.entities.BoxAndSettings
import com.kbcoding.androiddevelopmentbasics.app.domain.boxes.entities.BoxesFilter
import com.kbcoding.androiddevelopmentbasics.app.ui.main.tabs.dashboard.BoxViewModel
import com.kbcoding.androiddevelopmentbasics.app.utils.requireValue
import com.kbcoding.androiddevelopmentbasics.testUtils.ViewModelTest
import com.kbcoding.androiddevelopmentbasics.testUtils.createBoxAndSettings
import io.mockk.every
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Test

class BoxViewModelTest : ViewModelTest() {

    lateinit var flow: MutableStateFlow<ResponseResult<List<BoxAndSettings>>>

    @MockK
    lateinit var boxesRepository: BoxesRepository

    lateinit var viewModel: BoxViewModel

    private val boxId = 1L
    private val anotherBoxId = 2L

    @Before
    fun setUp() {
        flow = MutableStateFlow(Pending())
        every {
            boxesRepository.getBoxesAndSettings(BoxesFilter.ONLY_ACTIVE)
        } returns flow
        viewModel = BoxViewModel(boxId, boxesRepository, accountsRepository, logger)

    }

    @Test
    fun shouldExitEventIsFiredAfterDisablingBox() {
        val listWithBox = listOf(
            createBoxAndSettings(id = boxId)
        )
        val listWithoutBox = listOf(
            createBoxAndSettings(id = anotherBoxId)
        )

        flow.value = Success(listWithBox)
        val shouldExitEvent1 = viewModel.shouldExitEvent.requireValue().get()!!
        flow.value = Success(listWithoutBox)
        val shouldExitEvent2 = viewModel.shouldExitEvent.requireValue().get()!!

        assertFalse(shouldExitEvent1)
        assertTrue(shouldExitEvent2)
    }

}