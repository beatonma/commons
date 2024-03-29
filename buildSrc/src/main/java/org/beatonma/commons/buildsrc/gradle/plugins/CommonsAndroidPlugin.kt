package org.beatonma.commons.buildsrc.gradle.plugins

import Dependencies
import Modules
import Plugins
import Versions
import com.android.build.gradle.BaseExtension
import org.beatonma.commons.buildsrc.Git
import org.beatonma.commons.buildsrc.config.Commons
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.plugins.PluginContainer
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.project

interface CommonsAndroidPlugin<T : BaseExtension> : AndroidProjectPlugin<T> {

    @Suppress("UNCHECKED_CAST")
    override val Project.android: T
        get() = extensions.findByName("android") as? T
            ?: error("Not an Android module: $name")

    override fun applyPlugins(plugins: PluginContainer) {
        with(plugins) {
            apply(Plugins.Kotlin.ANDROID)
            apply(Plugins.Kotlin.KAPT)
            apply(Plugins.VERSIONS)
        }
        super.applyPlugins(plugins)
    }

    override fun applyAndroidConfig(android: T, target: Project) {
        val git = Git.resolveData(target)

        with(android) {
            compileSdkVersion(Commons.Sdk.COMPILE)
            defaultConfig {
                versionCode = git.commitCount
                versionName = git.tag

                minSdk = Commons.Sdk.MIN
                targetSdk = Commons.Sdk.TARGET

                buildConfigStrings(
                    "VERSION_NAME" to git.tag,
                    "GIT_SHA" to git.sha
                )
                buildConfigInts(
                    "VERSION_CODE" to git.commitCount
                )

                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                vectorDrawables.useSupportLibrary = true
            }

            buildTypes {
                getByName("debug") {
                    isDebuggable = true
                }

                getByName("release") {
                    isDebuggable = false
                }
            }

            compileOptions {
                sourceCompatibility = Versions.JAVA
                targetCompatibility = Versions.JAVA
            }

            kotlinOptions(target) {
                jvmTarget = Versions.JAVA.toString()
                languageVersion = Versions.KOTLIN_LANGUAGE_VERSION
                freeCompilerArgs = freeCompilerArgs + listOf(
                    "-Xopt-in=kotlin.RequiresOptIn", // Hide warnings about @OptIn annotations.
                )
            }

            packagingOptions {
                resources {
                    excludes += setOf(
                        "META-INF/DEPENDENCIES",
                        "META-INF/LICENSE",
                        "META-INF/NOTICE",
                        "META-INF/*.txt",
                        "META-INF/ASL2.0",
                        "META-INF/AL2.0",
                        "META-INF/LGPL2.1",
                        "META-INF/*.kotlin_module",
                        "META-INF/licenses/*",
                        "**/attach_hotspot_windows.dll"
                    )
                }
            }
        }
    }

    override fun applyRepositories(repositories: RepositoryHandler) {
        with(repositories) {
            mavenCentral()
        }
    }

    override fun applyDependencies(dependencies: DependencyHandlerScope) {
        with(dependencies) {
            unitTest {
                implementations(
                    project(Modules.Test.toString()),
                    Dependencies.Test.Jetpack.CORE,
                    Dependencies.Test.JUNIT
                )
            }

            instrumentationTest {
                implementations(
                    project(Modules.Test.toString()),
                    Dependencies.Test.Jetpack.CORE,
                    Dependencies.Test.Jetpack.RUNNER,
                    Dependencies.Test.JUNIT
                )
            }

            main {
                implementations(
                    Dependencies.Kotlin.STDLIB
                )
            }
        }
    }
}
