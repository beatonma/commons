package org.beatonma.commons.test.extensions


/**
 * Use reflection to change the value of a private/final property of this object
 * If the property was final before the call it will be restored to final afterwards
 *
 * Useful for replacing a private object with a mocked version.
 */
fun Any.editFinalField(fieldName: String, value: Any?) {
    this::class.java.getDeclaredField(fieldName).apply {
        val accessible = isAccessible
        isAccessible = true
        set(this@editFinalField, value)
        isAccessible = accessible
    }
}

inline fun <reified T> Any.invokePrivateMethod(methodName: String,
                            parameterClasses: Array<Class<*>>,
                            parameterValues: Array<Any?>): T? {
    this::class.java.getDeclaredMethod(methodName, *parameterClasses).apply {
        val accessible = isAccessible
        isAccessible = true
        val result = invoke(this@invokePrivateMethod, *parameterValues)
        isAccessible = accessible
        return result as? T
    }
}
