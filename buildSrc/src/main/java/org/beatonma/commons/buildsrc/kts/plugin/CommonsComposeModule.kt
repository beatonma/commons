package org.beatonma.commons.buildsrc.kts.plugin

import Dependencies
import Versions
import com.android.build.gradle.BaseExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.plugins.PluginContainer
import org.gradle.kotlin.dsl.DependencyHandlerScope

class CommonsComposeModule : SimpleAndroidProjectPlugin() {

    override fun applyPlugins(plugins: PluginContainer) {
        with(plugins) {
            apply("org.jetbrains.kotlin.android")
        }
    }

    override fun applyRepositories(repositories: RepositoryHandler) {
    }

    override fun applyDependencies(dependencies: DependencyHandlerScope) {
        with(dependencies) {
            main {
                implementations(
                    Dependencies.AndroidX.Compose.UI,
                    Dependencies.AndroidX.Compose.FOUNDATION,
                    Dependencies.AndroidX.Compose.MATERIAL,
                    Dependencies.AndroidX.Compose.TOOLING
                )
            }
        }
    }

    override fun applyAndroidConfig(android: BaseExtension, target: Project) {
        with(android) {
            buildFeatures.compose = true

            kotlinOptions(target) {
                useIR = true
                jvmTarget = Versions.JAVA.toString()
                freeCompilerArgs = freeCompilerArgs + listOf(
                    "-Xallow-jvm-ir-dependencies",
                    "-Xskip-prerelease-check"
                )
            }

            composeOptions {
                kotlinCompilerVersion = Versions.KOTLIN
                kotlinCompilerExtensionVersion = Versions.AX_COMPOSE
            }
        }
    }
}
