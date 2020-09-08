package org.beatonma.commons.buildsrc.kts.plugin

import com.android.build.gradle.BaseExtension
import com.android.build.gradle.internal.dsl.DefaultConfig
import org.beatonma.commons.buildsrc.kts.extensions.DebugDependencyScope
import org.beatonma.commons.buildsrc.kts.extensions.InstrumentationTestDependencyScope
import org.beatonma.commons.buildsrc.kts.extensions.MainDependencyScope
import org.beatonma.commons.buildsrc.kts.extensions.UnitTestDependencyScope
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.plugins.PluginContainer
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
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

    protected fun T.kotlinOptions(project: Project, block: KotlinJvmOptions.() -> Unit) {
        project.tasks.withType(KotlinCompile::class.java).configureEach {
            kotlinOptions {
                block()
            }
        }
    }
}

abstract class SimpleAndroidProjectPlugin : AndroidProjectPlugin<BaseExtension>()

internal fun DependencyHandlerScope.main(
    block: MainDependencyScope.() -> Unit
) = MainDependencyScope().block()

internal fun DependencyHandlerScope.debug(
    block: DebugDependencyScope.() -> Unit
) = DebugDependencyScope().block()

internal fun DependencyHandlerScope.unitTest(
    block: UnitTestDependencyScope.() -> Unit
) = UnitTestDependencyScope().block()

internal fun DependencyHandlerScope.instrumentationTest(
    block: InstrumentationTestDependencyScope.() -> Unit
) = InstrumentationTestDependencyScope().block()

