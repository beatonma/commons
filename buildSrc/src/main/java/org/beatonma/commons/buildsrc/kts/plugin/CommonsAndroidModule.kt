package org.beatonma.commons.buildsrc.kts.plugin

import Dependencies
import Versions
import com.android.build.gradle.BaseExtension
import org.beatonma.commons.buildsrc.Commons
import org.beatonma.commons.buildsrc.Git
import org.beatonma.commons.buildsrc.kts.extensions.debug
import org.beatonma.commons.buildsrc.kts.extensions.release
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.plugins.PluginContainer
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.project
import java.text.SimpleDateFormat
import java.util.*

abstract class CommonsAndroidModule<T : BaseExtension> : AndroidProjectPlugin<T>() {

    override val Project.android: T
        get() = extensions.findByName("android") as? T
            ?: error("Not an Android module: $name")

    open val timestamp: String by lazy {
        SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())
    }

    override fun applyPlugins(plugins: PluginContainer) {
        with(plugins) {
            apply("kotlin-android")
            apply("kotlin-android-extensions")
            apply("kotlin-kapt")
            apply("com.github.ben-manes.versions")
        }
    }

    override fun applyAndroidConfig(android: T, target: Project) {
        val git = Git.resolveData(target)

        with(android) {
            compileSdkVersion(Commons.Sdk.COMPILE)
            defaultConfig {
                versionCode = git.commitCount
                versionName = git.tag

                minSdkVersion(Commons.Sdk.MIN)
                targetSdkVersion(Commons.Sdk.TARGET)

                buildConfigStrings(
                    "VERSION_NAME" to git.tag,
                    "GIT_SHA" to git.sha
                )
                buildConfigInts(
                    "VERSION_CODE" to git.commitCount
                )

                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }

            buildTypes {
                debug {
                    isDebuggable = true
                }

                release {
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
                    "-XXLanguage:+InlineClasses"
                )
            }
        }
    }

    override fun applyRepositories(repositories: RepositoryHandler) {
        with(repositories) {
            maven("https://dl.bintray.com/kotlin/kotlin-eap")
            mavenCentral()
        }
    }

    override fun applyDependencies(dependencies: DependencyHandlerScope) {
        with(dependencies) {
            unitTest {
                implementations(
                    project(":test"),
                    Dependencies.Test.AndroidX.CORE,
                    Dependencies.Test.JUNIT
                )
            }

            instrumentationTest {
                implementations(
                    project(":test"),
                    Dependencies.Test.AndroidX.CORE,
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
