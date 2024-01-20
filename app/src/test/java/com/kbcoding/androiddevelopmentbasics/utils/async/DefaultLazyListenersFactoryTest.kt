package com.kbcoding.androiddevelopmentbasics.utils.async

import com.kbcoding.androiddevelopmentbasics.app.domain.Success
import com.kbcoding.androiddevelopmentbasics.app.utils.async.DefaultLazyListenersFactory
import com.kbcoding.androiddevelopmentbasics.app.utils.async.LazyListenersSubject
import com.kbcoding.androiddevelopmentbasics.app.utils.async.ValueListener
import com.kbcoding.androiddevelopmentbasics.app.utils.async.ValueLoader
import com.kbcoding.androiddevelopmentbasics.testUtils.immediateExecutorService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test


class DefaultLazyListenersFactoryTest {

    @Test
    fun createLazyListenersSubject() {
        val factory = DefaultLazyListenersFactory()
        val loader: ValueLoader<String, String> = mockk()
        val listener: ValueListener<String> = mockk(relaxed = true)
        every { loader("arg") } returns "result"

        val subject: LazyListenersSubject<String, String> =
            factory.createLazyListenersSubject(
                loaderExecutor = immediateExecutorService(),
                handlerExecutor = immediateExecutorService(),
                loader = loader
            )
        subject.addListener("arg", listener)

        verify {
            listener(Success("result"))
        }
    }

}