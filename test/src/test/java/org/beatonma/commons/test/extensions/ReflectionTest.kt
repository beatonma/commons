package org.beatonma.commons.test.extensions

import androidx.test.filters.SmallTest
import org.beatonma.commons.test.extensions.assertions.shouldbe
import org.junit.Test

private class ExampleClass {
    val publicVal: Int = 1
    private val privateVal: Int = 2
    private var privateVar: Int = 3
    var publicVar: Int = 4
        private set

    fun privateFieldTotal() = privateVal + privateVar
    private fun privateMultiply_ReturnInt(first: Int, second: Int) = first * second
    private fun privateSetPrivateVar_returnUnit(value: Int) {
        publicVar = value
    }
}

@SmallTest
class ReflectionTest {
    @Test
    fun editFinalField() {
        val exampleClass = ExampleClass()

        // Test updating a public final field
        exampleClass.editFinalField("publicVal", 8)
        exampleClass.publicVal shouldbe 8

        // Test updating a private final field
        exampleClass.editFinalField("privateVal", 9)
        exampleClass.privateFieldTotal() shouldbe (9 + 3)

//        // Test updating a private mutable field
        exampleClass.editFinalField("privateVar", 13)
        exampleClass.privateFieldTotal() shouldbe 9 + 13
    }

    @Test
    fun invokePrivateMethod() {
        val exampleClass = ExampleClass()

        val privateMethodMultiplyResult = exampleClass.invokePrivateMethod<Int>(
                "privateMultiply_ReturnInt",
                arrayOf(Int::class.java, Int::class.java),
                arrayOf(7, 3))
        privateMethodMultiplyResult shouldbe (7 * 3)

        exampleClass.invokePrivateMethod<Unit>(
                "privateSetPrivateVar_returnUnit",
                arrayOf(Int::class.java),
                arrayOf(17)
        )
        exampleClass.publicVar shouldbe 17
    }
}
