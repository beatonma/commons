package org.beatonma.commons.test

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import kotlin.reflect.KClass

/**
 * https://medium.com/@fanisveizis/boilerplate-free-proxy-fakes-ec0f09ce6957
 *
 * Creates a fake implementation of the given class using any method implementations
 * passed in [overrides].
 *
 * e.g. fakeOf(MyClass::class, object {
 *          fun myFunction(a: Int) = a + 1
 *      }
 */
@Suppress("UNCHECKED_CAST")
fun <T : Any> Any.fakeOf(type: KClass<T>, overrides: Any): T {
    return Proxy.newProxyInstance(
        javaClass.classLoader,
        arrayOf(type.java),
        FakeHandler(overrides)
    ) as T
}


private class FakeHandler(private val mimic: Any) : InvocationHandler {
    private fun getOverride(
        name: String,
        params: Array<Class<*>>?): Method? {

        val type = mimic::class.java
        return try {
            if (!params.isNullOrEmpty()) {
                type.getMethod(name, *params)
            } else {
                type.getMethod(name)
            }
        } catch (_: Exception) {
            null
        }
    }

    override fun invoke(
        proxy: Any,
        method: Method,
        args: Array<out Any>?
    ): Any? {

        val override = getOverride(
            method.name,
            method.parameterTypes)

        return if (override != null) {
            if (args != null) {
                override.invoke(mimic, *args)
            } else {
                override.invoke(mimic)
            }
        } else if (method.returnType != Void.TYPE) {
            throw IllegalStateException(
                "Invoked a non-faked method (\"${method.name}\") " +
                        "with a return type (\"${method.returnType}\").")
        } else {
            null
        }
    }
}
