package org.beatonma.commons.buildsrc.kts.plugin

import com.android.build.gradle.BaseExtension
import com.android.build.gradle.internal.dsl.DefaultConfig
import org.beatonma.commons.buildsrc.kts.extensions.DebugDependencies
import org.beatonma.commons.buildsrc.kts.extensions.InstrumentationTestDependencies
import org.beatonma.commons.buildsrc.kts.extensions.MainDependencies
import org.beatonma.commons.buildsrc.kts.extensions.UnitTestDependencies
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.plugins.PluginContainer
import org.gradle.kotlin.dsl.DependencyHandlerScope
import java.util.*

abstract class ProjectPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        applyPlugins(target.plugins)
        applyRepositories(target.repositories)
        applyDependencies(DependencyHandlerScope.of(target.dependencies))
    }

    open fun applyPlugins(plugins: PluginContainer) {}
    open fun applyRepositories(repositories: RepositoryHandler) {}
    open fun applyDependencies(dependencies: DependencyHandlerScope) {}
}

abstract class AndroidProjectPlugin<T : BaseExtension> : ProjectPlugin() {

    open val Project.android: T
        get() = extensions.findByName("android") as? T
            ?: error("Not an Android module: $name")

    abstract fun applyAndroidConfig(android: T, target: Project)

    override fun apply(target: Project) {
        super.apply(target)
        applyAndroidConfig(target.android, target)
    }

    protected fun DefaultConfig.buildConfigStrings(vararg mapping: Pair<String, String>) {
        mapping.forEach { (key, value) ->
            buildConfigField("String", key.toUpperCase(Locale.getDefault()), "\"$value\"")
        }
    }

    protected fun DefaultConfig.buildConfigInts(vararg mapping: Pair<String, Int>) {
        mapping.forEach { (key, value) ->
            buildConfigField("int", key.toUpperCase(Locale.getDefault()), "$value")
        }
    }
}

abstract class SimpleAndroidProjectPlugin : AndroidProjectPlugin<BaseExtension>()

internal fun DependencyHandlerScope.main(
    block: MainDependencies.() -> Unit
) = MainDependencies().block()

internal fun DependencyHandlerScope.debug(
    block: DebugDependencies.() -> Unit
) = DebugDependencies().block()

internal fun DependencyHandlerScope.unitTest(
    block: UnitTestDependencies.() -> Unit
) = UnitTestDependencies().block()

internal fun DependencyHandlerScope.instrumentationTest(
    block: InstrumentationTestDependencies.() -> Unit
) = InstrumentationTestDependencies().block()

