package com.kbcoding.androiddevelopmentbasics

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.just
import io.mockk.runs
import io.mockk.slot
import io.mockk.spyk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.Executor
import kotlin.random.Random

class MockkInitAnnotationDemoTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    lateinit var executor: Executor

    @MockK
    lateinit var errorHandler: ErrorHandler<String>

    @RelaxedMockK
    lateinit var bar: Bar

    @InjectMockKs
    lateinit var resourceManager: ResourceManager<String>


    interface Bar {

        fun print(value: Int): Boolean
        fun foo()
        fun getInt(): Int
        fun getBoolean(): Boolean
    }

    @Test
    fun testDemoSpy() {
        val resourceManagerSpy = spyk(resourceManager)
        every { resourceManagerSpy.destroy() } answers {
            println("destroy() call has been replaced by the spy")
        }
        resourceManagerSpy.destroy()

        verify(exactly = 1) { resourceManagerSpy.destroy() }
    }

    @Test
    fun testDemoMockkRelaxedTrue() {
        bar.foo()
        println("Int: ${bar.getInt()}")
        println("Boolean: ${bar.getBoolean()}")
    }

    @Test
    fun testDemoMockkWithEvery() {
        every { bar.foo() } answers {
            println("foo() method has been called")
        }
        every { bar.getBoolean() } returns true
        every { bar.getInt() } returns 479

        bar.foo()
        println("Int: ${bar.getInt()}")
        println("Boolean: ${bar.getBoolean()}")
    }

    @Test
    fun testDemoMockkWithEveryAnswer() {
        every { bar.foo() } answers {
            println("foo() method has been called")
        }
        every { bar.getBoolean() } answers {
            Random.nextBoolean()
        }
        every { executor.execute(any()) } answers {
            firstArg<Runnable>().run()
        }

        bar.foo()
        println("Int: ${bar.getInt()}")
        println("Boolean: ${bar.getBoolean()}")
    }

    @Test
    fun testDemoMockkWithMatchers() {

        every { bar.print(less(0)) } returns false
        every { bar.print(more(10, andEquals = true)) } answers {
            println(firstArg<Int>())
            true
        }

        println("Arg -1 returns: ${bar.print(-1)}")
        println("Arg 11 returns: ${bar.print(11)}")
    }

    @Test
    fun testDemoMockkWithMatchersInterceptorsSlot() {

        val intSlot = slot<Int>()
        every { bar.print(capture(intSlot)) } answers {
            println("Arg: ${intSlot.captured}")
            intSlot.captured < 0
        }
        bar.print(123)
        println("Captured arg: ${intSlot.captured}")
    }

    @Test
    fun testDemoMockkWithMatchersInterceptorsSlotList() {
        val intSlot = mutableListOf<Int>()
        every { bar.print(capture(intSlot)) } returns true

        bar.print(123)
        bar.print(1)
        bar.print(2)
        bar.print(3)

        println("Captured arg: ${intSlot.joinToString()}")
    }

    @Test
    fun testDemoMockkWithEveryJustRuns() {
        every { bar.foo() } just runs

        bar.foo()
    }

    @Test
    fun testDemoMockkWithEveryAndThan() {
        every { bar.foo() } answers {
            println("foo() method has been called")
        }

        every { bar.getBoolean() } returns true andThen false andThen true
        every { bar.getInt() } returns 479 andThen 4 andThen 7 andThen 9

        bar.foo()
        println("Int: ${bar.getInt()}")
        println("Int2: ${bar.getInt()}")
        println("Int3: ${bar.getInt()}")
        println("Int4: ${bar.getInt()}")
        println("Boolean: ${bar.getBoolean()}")
        println("Boolean2: ${bar.getBoolean()}")
        println("Boolean3: ${bar.getBoolean()}")
    }

    @Test
    fun testDemoMockkWithEveryAndReturnsMany() {
        every { bar.foo() } answers {
            println("foo() method has been called")
        }

        every { bar.getBoolean() } returnsMany listOf(
            true,
            false,
            true
        )
        every { bar.getInt() } returnsMany listOf(479, 4, 7, 9)

        bar.foo()
        println("Int: ${bar.getInt()}")
        println("Int2: ${bar.getInt()}")
        println("Int3: ${bar.getInt()}")
        println("Int4: ${bar.getInt()}")
        println("Boolean: ${bar.getBoolean()}")
        println("Boolean2: ${bar.getBoolean()}")
        println("Boolean3: ${bar.getBoolean()}")
    }
}