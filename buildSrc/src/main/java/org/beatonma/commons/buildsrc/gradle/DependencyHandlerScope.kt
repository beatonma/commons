package org.beatonma.commons.buildsrc.gradle

import org.gradle.kotlin.dsl.DependencyHandlerScope

/**
 * Simple DSL for organising dependencies{ } by build type.
 *
 * e.g.
 *      main {
 *          annotationProcessors(
 *              Dependencies.Dagger.ANNOTATION_PROCESSOR,
 *              Dependencies.Dagger.COMPILER,
 *              ...
 *          )
 *
 *          implementations(
 *              Dependencies.Kotlin.STDLIB,
 *              Dependencies.Kotlin.Coroutines.ANDROID,
 *              ...
 *          )
 *
 *          apis(
 *              ...
 *          )
 *      }
 *
 *      unitTest {
 *          ...
 *      }
 *
 *      instrumentationTest {
 *          ...
 *      }
 *
 *      debug {
 *          ...
 *      }
 */

fun DependencyHandlerScope.main(
    block: MainDependencyScope.() -> Unit
) = MainDependencyScope().block()

fun DependencyHandlerScope.debug(
    block: DebugDependencyScope.() -> Unit
) = DebugDependencyScope().block()

fun DependencyHandlerScope.unitTest(
    block: UnitTestDependencyScope.() -> Unit
) = UnitTestDependencyScope().block()

fun DependencyHandlerScope.instrumentationTest(
    block: InstrumentationTestDependencyScope.() -> Unit
) = InstrumentationTestDependencyScope().block()

interface DependencyScope {

    fun DependencyHandlerScope.ap(vararg modules: Any) = annotationProcessors(modules)
    fun DependencyHandlerScope.annotationProcessors(vararg modules: Any)
    fun DependencyHandlerScope.implementations(vararg modules: Any)
    fun DependencyHandlerScope.apis(vararg modules: Any)
}

class MainDependencyScope : DependencyScope {

    override fun DependencyHandlerScope.annotationProcessors(vararg modules: Any) =
        modules.forEach { "kapt"(it) }

    override fun DependencyHandlerScope.implementations(vararg modules: Any) =
        modules.forEach { "implementation"(it) }

    override fun DependencyHandlerScope.apis(vararg modules: Any) = modules.forEach { "api"(it) }
}

class DebugDependencyScope : DependencyScope {

    override fun DependencyHandlerScope.annotationProcessors(vararg modules: Any) =
        modules.forEach { "kaptDebug"(it) }

    override fun DependencyHandlerScope.implementations(vararg modules: Any) =
        modules.forEach { "debugImplementation"(it) }

    override fun DependencyHandlerScope.apis(vararg modules: Any) =
        modules.forEach { "debugApi"(it) }
}

class UnitTestDependencyScope : DependencyScope {

    override fun DependencyHandlerScope.annotationProcessors(vararg modules: Any) =
        modules.forEach { "kaptTest"(it) }

    override fun DependencyHandlerScope.implementations(vararg modules: Any) =
        modules.forEach { "testImplementation"(it) }

    override fun DependencyHandlerScope.apis(vararg modules: Any) =
        modules.forEach { "testApi"(it) }
}

class InstrumentationTestDependencyScope : DependencyScope {

    override fun DependencyHandlerScope.annotationProcessors(vararg modules: Any) =
        modules.forEach { "kaptAndroidTest"(it) }

    override fun DependencyHandlerScope.implementations(vararg modules: Any) =
        modules.forEach { "androidTestImplementation"(it) }

    override fun DependencyHandlerScope.apis(vararg modules: Any) =
        modules.forEach { "androidTestApi"(it) }
}
