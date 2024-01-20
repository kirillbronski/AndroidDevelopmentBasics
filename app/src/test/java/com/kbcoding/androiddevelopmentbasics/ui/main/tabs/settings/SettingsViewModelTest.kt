package com.kbcoding.androiddevelopmentbasics.ui.main.tabs.settings

import com.kbcoding.androiddevelopmentbasics.app.domain.Pending
import com.kbcoding.androiddevelopmentbasics.app.domain.ResponseResult
import com.kbcoding.androiddevelopmentbasics.app.domain.Success
import com.kbcoding.androiddevelopmentbasics.app.domain.boxes.BoxesRepository
import com.kbcoding.androiddevelopmentbasics.app.domain.boxes.entities.BoxAndSettings
import com.kbcoding.androiddevelopmentbasics.app.domain.boxes.entities.BoxesFilter
import com.kbcoding.androiddevelopmentbasics.app.ui.main.tabs.settings.SettingsViewModel
import com.kbcoding.androiddevelopmentbasics.app.utils.requireValue
import com.kbcoding.androiddevelopmentbasics.core.ViewModelExceptionsTest
import com.kbcoding.androiddevelopmentbasics.testUtils.ViewModelTest
import com.kbcoding.androiddevelopmentbasics.testUtils.arranged
import com.kbcoding.androiddevelopmentbasics.testUtils.createBox
import com.kbcoding.androiddevelopmentbasics.testUtils.createBoxAndSettings
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Test

class SettingsViewModelTest : ViewModelTest() {

    private lateinit var boxesFlow: MutableStateFlow<ResponseResult<List<BoxAndSettings>>>

    private lateinit var boxesRepository: BoxesRepository

    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setUp() {
        boxesFlow = MutableStateFlow(Pending())
        boxesRepository = createBoxesRepository(boxesFlow)
        viewModel = SettingsViewModel(boxesRepository, accountsRepository, logger)
    }

    @Test
    fun enableBoxEnablesBox() {
        val box = createBox()

        viewModel.enableBox(box)

        coVerify(exactly = 1) {
            boxesRepository.activateBox(box)
        }
    }

    @Test
    fun disableBoxDisablesBox() {
        val box = createBox()

        viewModel.disableBox(box)

        coVerify(exactly = 1) {
            boxesRepository.deactivateBox(box)
        }
    }

    @Test
    fun tryAgainReloadsData() {
        arranged()

        viewModel.tryAgain()

        coVerify(exactly = 1) {
            boxesRepository.reload(BoxesFilter.ALL)
        }
    }

    @Test
    fun boxSettingsReturnsDataFromRepository() {
        val expectedBoxes1 = Pending<List<BoxAndSettings>>()
        val expectedBoxes2 = listOf(
            createBoxAndSettings(id = 1, name = "Box1", isActive = true),
            createBoxAndSettings(id = 2, name = "Box2", isActive = true)
        )
        val expectedBoxes3 = listOf(
            createBoxAndSettings(id = 2, name = "Box2", isActive = false),
            createBoxAndSettings(id = 3, name = "Box3", isActive = false)
        )

        boxesFlow.value = Pending()
        val result1 = viewModel.boxSettings.requireValue()
        boxesFlow.value = Success(expectedBoxes2)
        val result2 = viewModel.boxSettings.requireValue()
        boxesFlow.value = Success(expectedBoxes3)
        val result3 = viewModel.boxSettings.requireValue()

        assertEquals(expectedBoxes1, result1)
        assertEquals(expectedBoxes2, result2.getValueOrNull())
        assertEquals(expectedBoxes3, result3.getValueOrNull())
    }

    abstract class SettingsViewModelExceptionsTest : ViewModelExceptionsTest<SettingsViewModel>() {
        lateinit var boxesRepository: BoxesRepository
        override lateinit var viewModel: SettingsViewModel

        @Before
        fun setUp() {
            boxesRepository = createBoxesRepository(flowOf())
            viewModel = SettingsViewModel(boxesRepository, accountsRepository, logger)
        }
    }

    class EnableBoxExceptionsTest : SettingsViewModelExceptionsTest() {
        override fun arrangeWithException(e: Exception) {
            coEvery { boxesRepository.activateBox(any()) } throws e
        }
        override fun act() {
            viewModel.enableBox(createBox())
        }
    }

    class DisableBoxExceptionsTest : SettingsViewModelExceptionsTest() {
        override fun arrangeWithException(e: Exception) {
            coEvery { boxesRepository.deactivateBox(any()) } throws e
        }
        override fun act() {
            viewModel.disableBox(createBox())
        }
    }

    class TryAgainExceptionsTest : SettingsViewModelExceptionsTest() {
        override fun arrangeWithException(e: Exception) {
            coEvery { boxesRepository.reload(any()) } throws e
        }
        override fun act() {
            viewModel.tryAgain()
        }
    }

    private companion object {
        fun createBoxesRepository(flow: Flow<ResponseResult<List<BoxAndSettings>>>): BoxesRepository {
            val repository = mockk<BoxesRepository>(relaxed = true)
            every { repository.getBoxesAndSettings(any()) } returns flow
            return repository
        }
    }

}