package org.beatonma.commons.buildsrc.kts.extensions

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
    block: MainDependencies.() -> Unit
) = MainDependencies().block()

fun DependencyHandlerScope.debug(
    block: DebugDependencies.() -> Unit
) = DebugDependencies().block()

fun DependencyHandlerScope.unitTest(
    block: UnitTestDependencies.() -> Unit
) = UnitTestDependencies().block()

fun DependencyHandlerScope.instrumentationTest(
    block: InstrumentationTestDependencies.() -> Unit
) = InstrumentationTestDependencies().block()


interface DependencyBlock {
    fun DependencyHandlerScope.ap(vararg modules: Any) = annotationProcessors(modules)
    fun DependencyHandlerScope.annotationProcessors(vararg modules: Any)
    fun DependencyHandlerScope.implementations(vararg modules: Any)
    fun DependencyHandlerScope.apis(vararg modules: Any)
}

class MainDependencies: DependencyBlock {
    override fun DependencyHandlerScope.annotationProcessors(vararg modules: Any) = modules.forEach { "kapt"(it) }

    override fun DependencyHandlerScope.implementations(vararg modules: Any) = modules.forEach { "implementation"(it) }
    override fun DependencyHandlerScope.apis(vararg modules: Any) = modules.forEach { "api"(it) }
}

class DebugDependencies: DependencyBlock {
    override fun DependencyHandlerScope.annotationProcessors(vararg modules: Any) = modules.forEach { "kaptDebug"(it) }
    override fun DependencyHandlerScope.implementations(vararg modules: Any) = modules.forEach { "debugImplementation"(it) }
    override fun DependencyHandlerScope.apis(vararg modules: Any) = modules.forEach { "debugApi"(it) }
}

class UnitTestDependencies: DependencyBlock {
    override fun DependencyHandlerScope.annotationProcessors(vararg modules: Any) = modules.forEach { "kaptTest"(it) }
    override fun DependencyHandlerScope.implementations(vararg modules: Any) = modules.forEach { "testImplementation"(it) }
    override fun DependencyHandlerScope.apis(vararg modules: Any) = modules.forEach { "testApi"(it) }
}

class InstrumentationTestDependencies: DependencyBlock {
    override fun DependencyHandlerScope.annotationProcessors(vararg modules: Any) = modules.forEach { "kaptAndroidTest"(it) }
    override fun DependencyHandlerScope.implementations(vararg modules: Any) = modules.forEach { "androidTestImplementation"(it) }
    override fun DependencyHandlerScope.apis(vararg modules: Any) = modules.forEach { "androidTestApi"(it) }
}
