package org.beatonma.commons.buildsrc.gradle.plugins

import com.android.build.gradle.BaseExtension
import com.android.build.gradle.internal.dsl.DefaultConfig
import org.beatonma.commons.buildsrc.gradle.DebugDependencyScope
import org.beatonma.commons.buildsrc.gradle.InstrumentationTestDependencyScope
import org.beatonma.commons.buildsrc.gradle.MainDependencyScope
import org.beatonma.commons.buildsrc.gradle.UnitTestDependencyScope
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.plugins.PluginContainer
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.Locale

interface ProjectPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        applyPlugins(target.plugins)
        applyRepositories(target.repositories)
        applyDependencies(DependencyHandlerScope.of(target.dependencies))
    }

    fun applyPlugins(plugins: PluginContainer) {}
    fun applyRepositories(repositories: RepositoryHandler) {}
    fun applyDependencies(dependencies: DependencyHandlerScope) {}
}

interface AndroidProjectPlugin<T : BaseExtension> : ProjectPlugin {

    @Suppress("UNCHECKED_CAST")
    val Project.android: T
        get() = extensions.findByName("android") as? T
            ?: error("Not an Android module: $name")

    fun applyAndroidConfig(android: T, target: Project)

    override fun apply(target: Project) {
        super.apply(target)
        applyAndroidConfig(target.android, target)
    }

    fun DefaultConfig.buildConfigStrings(vararg mapping: Pair<String, String>) {
        mapping.forEach { (key, value) ->
            buildConfigField("String", key.toUpperCase(Locale.getDefault()), "\"$value\"")
        }
    }

    fun DefaultConfig.buildConfigInts(vararg mapping: Pair<String, Int>) {
        mapping.forEach { (key, value) ->
            buildConfigField("int", key.toUpperCase(Locale.getDefault()), "$value")
        }
    }

    @Suppress("unused")
    fun T.kotlinOptions(project: Project, block: KotlinJvmOptions.() -> Unit) {
        project.tasks.withType(KotlinCompile::class.java).configureEach {
            kotlinOptions {
                block()
            }
        }
    }
}

interface SimpleAndroidProjectPlugin : AndroidProjectPlugin<BaseExtension>

@Suppress("unused")
internal fun DependencyHandlerScope.main(
    block: MainDependencyScope.() -> Unit
) = MainDependencyScope().block()

@Suppress("unused")
internal fun DependencyHandlerScope.debug(
    block: DebugDependencyScope.() -> Unit
) = DebugDependencyScope().block()

@Suppress("unused")
internal fun DependencyHandlerScope.unitTest(
    block: UnitTestDependencyScope.() -> Unit
) = UnitTestDependencyScope().block()

@Suppress("unused")
internal fun DependencyHandlerScope.instrumentationTest(
    block: InstrumentationTestDependencyScope.() -> Unit
) = InstrumentationTestDependencyScope().block()
