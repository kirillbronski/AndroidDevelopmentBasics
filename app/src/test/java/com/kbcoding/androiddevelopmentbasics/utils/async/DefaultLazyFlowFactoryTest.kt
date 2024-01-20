package com.kbcoding.androiddevelopmentbasics.utils.async

import com.kbcoding.androiddevelopmentbasics.app.domain.Pending
import com.kbcoding.androiddevelopmentbasics.app.domain.Success
import com.kbcoding.androiddevelopmentbasics.app.utils.async.DefaultLazyFlowFactory
import com.kbcoding.androiddevelopmentbasics.app.utils.async.DefaultLazyListenersFactory
import com.kbcoding.androiddevelopmentbasics.app.utils.async.LazyFlowSubject
import com.kbcoding.androiddevelopmentbasics.app.utils.async.SuspendValueLoader
import com.kbcoding.androiddevelopmentbasics.testUtils.immediateExecutorService
import com.kbcoding.androiddevelopmentbasics.testUtils.runFlowTest
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import junit.framework.Assert
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.concurrent.Executors

@ExperimentalCoroutinesApi
class DefaultLazyFlowFactoryTest {

    @Before
    fun setUp() {
        mockkStatic(Executors::class)
        every { Executors.newSingleThreadExecutor() } returns immediateExecutorService()
    }

    @After
    fun tearDown() {
        unmockkStatic(Executors::class)
    }

    @Test
    fun createLazyFlowSubject() = runFlowTest {
        val factory = DefaultLazyFlowFactory(DefaultLazyListenersFactory())
        val loader: SuspendValueLoader<String, String> = mockk()
        coEvery { loader("arg") } returns "result"

        val subject: LazyFlowSubject<String, String> =
            factory.createLazyFlowSubject(loader)
        val collectedResults =
            subject.listen("arg").startCollecting()

        assertEquals(
            listOf(Pending(), Success("result")),
            collectedResults
        )
    }
}