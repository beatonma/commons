package org.beatonma.commons.buildsrc.gradle.plugins

import Dependencies
import Modules
import Versions
import com.android.build.gradle.BaseExtension
import org.beatonma.commons.buildsrc.gradle.project
import org.gradle.api.Project
import org.gradle.kotlin.dsl.DependencyHandlerScope


/**
 * Configure a project for use with Jetpack Compose.
 */
class CommonsComposePlugin : SimpleAndroidProjectPlugin {
    override fun applyDependencies(dependencies: DependencyHandlerScope) {
        with(dependencies) {
            instrumentationTest {
                implementations(
                    Dependencies.Jetpack.Compose.TEST,
                    Dependencies.Jetpack.Compose.TEST_JUNIT,
                    Dependencies.Jetpack.Compose.TEST_MANIFEST,
                    project(Modules.TestCompose),
                )
            }

            debug {
                implementations(
                    Dependencies.Jetpack.Compose.TOOLING,
                    Dependencies.Kotlin.REFLECT,
                )
            }

            main {
                implementations(
                    Dependencies.Jetpack.Compose.COMPILER,
                    Dependencies.Jetpack.Compose.FOUNDATION,
                    Dependencies.Jetpack.Compose.MATERIAL,
                    Dependencies.Jetpack.Compose.RUNTIME,
                    Dependencies.Jetpack.Compose.TOOLING_PREVIEW,
                    Dependencies.Jetpack.Compose.UI,
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
                    "plugin:androidx.compose.compiler.plugins.kotlin:suppressKotlinVersionCompatibilityCheck=true",
                )
                languageVersion = Versions.KOTLIN_LANGUAGE_VERSION
            }

            composeOptions {
                kotlinCompilerExtensionVersion = Versions.Jetpack.Compose.COMPOSE
            }
        }
    }
}
