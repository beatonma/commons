package org.beatonma.commons.buildsrc.kts.plugin

import Dependencies
import Modules
import Plugins
import Versions
import com.android.build.gradle.BaseExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.plugins.PluginContainer
import org.gradle.kotlin.dsl.DependencyHandlerScope
import project

class CommonsComposeModule : SimpleAndroidProjectPlugin() {

    override fun applyPlugins(plugins: PluginContainer) {
        with(plugins) {
            apply(Plugins.Kotlin.ANDROID)
        }
        super.applyPlugins(plugins)
    }

    override fun applyRepositories(repositories: RepositoryHandler) {
    }

    override fun applyDependencies(dependencies: DependencyHandlerScope) {
        with(dependencies) {
            instrumentationTest {
                implementations(
                    Dependencies.Jetpack.Compose.TEST,
                    Dependencies.Jetpack.Compose.TEST_JUNIT,
                    project(Modules.TestCompose)
                )
            }

            debug {
                implementations(
                    Dependencies.Jetpack.Compose.TOOLING,
                    Dependencies.Kotlin.REFLECT
                )
            }

            main {
                implementations(
                    Dependencies.Jetpack.Compose.COMPILER,
                    Dependencies.Jetpack.Compose.FOUNDATION,
                    Dependencies.Jetpack.Compose.MATERIAL,
                    Dependencies.Jetpack.Compose.UI
                )
            }
        }
    }

    override fun applyAndroidConfig(android: BaseExtension, target: Project) {
        with(android) {
            buildFeatures.compose = true

            kotlinOptions(target) {
                jvmTarget = Versions.JAVA.toString()
                freeCompilerArgs = freeCompilerArgs + listOf(
                    "-Xskip-prerelease-check",
                    "-P",
                    "plugin:androidx.compose.compiler.plugins.kotlin:suppressKotlinVersionCompatibilityCheck=true"
                )
                languageVersion = Versions.KOTLIN_LANGUAGE_VERSION
            }

            composeOptions {
                kotlinCompilerExtensionVersion = Versions.Jetpack.Compose.COMPOSE
            }
        }
    }
}
