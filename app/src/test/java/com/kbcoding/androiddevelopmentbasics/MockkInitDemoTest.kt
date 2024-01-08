package com.kbcoding.androiddevelopmentbasics

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.Executor

class MockkInitDemoTest {

    interface Bar {
        fun foo()
        fun getInt(): Int
        fun getBoolean(): Boolean
    }

    @Test
    fun testDemo() {
        val testExecutor = mockk<Executor>()
        val testErrorHandler = mockk<ErrorHandler<String>>()
        val resourceManager = ResourceManager(
            executor = testExecutor,
            errorHandler = testErrorHandler
        )
        val resourceManagerSpy = spyk(resourceManager)
        every { resourceManagerSpy.destroy() } answers {
            println("destroy() call has been replaced by the spy")
        }
        resourceManagerSpy.destroy()

        verify(exactly = 1) { resourceManagerSpy.destroy() }
    }

    @Test
    fun testDemoMockkRelaxedTrue() {
        val testExecutor = mockk<Executor>()
        val testErrorHandler = mockk<ErrorHandler<String>>()
        val resourceManager = ResourceManager(
            executor = testExecutor,
            errorHandler = testErrorHandler
        )
        val bar = mockk<Bar>(relaxed = true)

        bar.foo()
        println("Int: ${bar.getInt()}")
        println("Boolean: ${bar.getBoolean()}")
    }

    @Test
    fun testDemoMockkWithEvery() {
        val testExecutor = mockk<Executor>()
        val testErrorHandler = mockk<ErrorHandler<String>>()
        val resourceManager = ResourceManager(
            executor = testExecutor,
            errorHandler = testErrorHandler
        )
        val bar = mockk<Bar>()
        every { bar.foo() } answers {
            println("foo() method has been called")
        }
//        every { bar.getBoolean() } answers { true }
//        every { bar.getInt() } answers { 479 }
        every { bar.getBoolean() } returns true
        every { bar.getInt() } returns 479

        bar.foo()
        println("Int: ${bar.getInt()}")
        println("Boolean: ${bar.getBoolean()}")
    }
}