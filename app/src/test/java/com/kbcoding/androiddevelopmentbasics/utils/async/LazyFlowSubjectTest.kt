package com.kbcoding.androiddevelopmentbasics.utils.async

import com.kbcoding.androiddevelopmentbasics.app.domain.Pending
import com.kbcoding.androiddevelopmentbasics.app.domain.Success
import com.kbcoding.androiddevelopmentbasics.app.utils.async.LazyFlowSubject
import com.kbcoding.androiddevelopmentbasics.app.utils.async.LazyListenersFactory
import com.kbcoding.androiddevelopmentbasics.app.utils.async.LazyListenersSubject
import com.kbcoding.androiddevelopmentbasics.app.utils.async.SuspendValueLoader
import com.kbcoding.androiddevelopmentbasics.app.utils.async.ValueListener
import com.kbcoding.androiddevelopmentbasics.app.utils.async.ValueLoader
import com.kbcoding.androiddevelopmentbasics.testUtils.immediateExecutorService
import com.kbcoding.androiddevelopmentbasics.testUtils.runFlowTest
import io.mockk.CapturingSlot
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.runs
import io.mockk.slot
import io.mockk.unmockkStatic
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.Executors

@ExperimentalCoroutinesApi
class LazyFlowSubjectTest {

    @get:Rule
    val rule = MockKRule(this)

    @RelaxedMockK
    lateinit var lazyListenersSubject: LazyListenersSubject<String, String>

    @MockK
    lateinit var lazyListenersFactory: LazyListenersFactory

    lateinit var loader: SuspendValueLoader<String, String>

    lateinit var lazyFlowSubject: LazyFlowSubject<String, String>

    @Before
    fun setUp() = runTest {
        loader = mockk(relaxed = true)
        every {
            lazyListenersFactory.createLazyListenersSubject<String, String>(any(), any(), any())
        } returns lazyListenersSubject

        lazyFlowSubject = LazyFlowSubject(lazyListenersFactory, loader)

        mockkStatic(Executors::class)
        every { Executors.newSingleThreadExecutor() } returns immediateExecutorService()
    }

    @After
    fun tearDown() {
        unmockkStatic(Executors::class)
    }

    @Test
    fun initCreatesLazyListenersSubjectWithValidLoader() {
        val slot: CapturingSlot<ValueLoader<String, String>> = slot()
        every {
            lazyListenersFactory.createLazyListenersSubject(
                any(), any(), capture(slot)
            )
        } returns mockk()
        coEvery { loader("arg") } returns "result"

        LazyFlowSubject(lazyListenersFactory, loader)
        val answer = slot.captured("arg")

        assertEquals("result", answer)
    }

    @Test
    fun reloadAllDelegatesCallToLazyListenersSubject() {

        lazyFlowSubject.reloadAll(silentMode = true)

        verify(exactly = 1) {
            lazyListenersSubject.reloadAll(silentMode = true)
        }
    }

    @Test
    fun reloadAllDoesNotUseSilentModeByDefault() {

        lazyFlowSubject.reloadAll()

        verify(exactly = 1) {
            lazyListenersSubject.reloadAll(silentMode = false)
        }

    }

    @Test
    fun reloadArgumentDelegatesCallToLazyListenersSubject() {

        lazyFlowSubject.reloadArgument("test")

        verify(exactly = 1) {
            lazyListenersSubject.reloadArgument("test")
        }
    }

    @Test
    fun updateAllValuesDelegatesCallToLazyListenersSubject() {

        lazyFlowSubject.updateAllValues("test")

        verify(exactly = 1) {
            lazyListenersSubject.updateAllValues("test")
        }
    }

    @Test
    fun listenDeliversResultsFromCallbackToFlow() = runFlowTest {
        val slot = captureAddListener("arg")

        val flow = lazyFlowSubject.listen("arg")
        val results = flow.startCollecting()
        slot.captured(Pending())
        slot.captured(Success("hi"))

        assertEquals(
            listOf(Pending(), Success("hi")),
            results
        )
    }

    @Test
    fun listenAfterCancellingSubscriptionRemovesCallback() = runFlowTest {
        val slot = captureAddListener("arg")

        val flow = lazyFlowSubject.listen("arg")
        val results = flow.startCollecting()
        slot.captured(Success("111"))
        flow.cancelCollecting()
        slot.captured(Success("222"))

        assertEquals(
            listOf(Success("111")),
            results
        )
        verify(exactly = 1) {
            lazyListenersSubject.removeListener("arg", refEq(slot.captured))
        }
    }

    private fun captureAddListener(arg: String): CapturingSlot<ValueListener<String>> {
        val slot: CapturingSlot<ValueListener<String>> = slot()
        every {
            lazyListenersSubject.addListener(arg, capture(slot))
        } just runs
        return slot
    }


}