package org.beatonma.commons.buildsrc.kts.plugin

import Dependencies
import Versions
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.internal.dsl.DefaultConfig
import org.beatonma.commons.buildsrc.Commons
import org.beatonma.commons.buildsrc.Git
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.project
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.text.SimpleDateFormat
import java.util.*

abstract class CommonsAndroidModule<T: BaseExtension>: Plugin<Project> {
    open val Project.android: T
        get() = extensions.findByName("android") as? T
            ?: error("Not an Android module: $name")

    open val timestamp: String by lazy {
        SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())
    }

    override fun apply(target: Project) {
        applyRepositories(target)
        applyPlugins(target)
        applyAndroidConfig(target)
        applyDependencies(target)
    }

    open fun applyPlugins(target: Project) {
        target.plugins.run {
            apply("kotlin-android")
            apply("kotlin-android-extensions")
            apply("kotlin-kapt")
            apply("com.github.ben-manes.versions")
        }
    }

    open fun applyAndroidConfig(target: Project) {
        val git = Git.resolveData(target)

        target.android.run {
            compileSdkVersion(Commons.Sdk.COMPILE)
            defaultConfig {
                versionCode = git.commitCount
                versionName = git.tag

                minSdkVersion(Commons.Sdk.MIN)
                targetSdkVersion(Commons.Sdk.TARGET)

                injectStrings(
                    "VERSION_NAME" to git.tag,
                    "GIT_SHA" to git.sha
                )
                injectInts(
                    "VERSION_CODE" to git.commitCount
                )

                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }

            compileOptions {
                sourceCompatibility = Versions.JAVA
                targetCompatibility = Versions.JAVA
            }

            target.tasks.withType(KotlinCompile::class.java).configureEach {
                kotlinOptions {
                    jvmTarget = "1.8"
                    languageVersion = "1.4"
                    freeCompilerArgs = listOf(
                        "-XXLanguage:+InlineClasses"
                    )
                }
            }
        }
    }


    open fun applyRepositories(target: Project) {
        target.repositories.run {
            maven("https://dl.bintray.com/kotlin/kotlin-eap")
            mavenCentral()
        }
    }

    open fun applyDependencies(target: Project) {
        target.dependencies {
            arrayOf(
                project(":test"),
                Dependencies.Test.AndroidX.CORE,
                Dependencies.Test.JUNIT
            ).forEach {
                "testImplementation"(it)
                "androidTestImplementation"(it)
            }

            arrayOf(
                Dependencies.Kotlin.STDLIB
            ).forEach { "implementation"(it) }
        }
    }

    protected fun DefaultConfig.injectStrings(vararg mapping: Pair<String, String>) {
        mapping.forEach { (key, value) ->
            buildConfigField("String", key.toUpperCase(Locale.getDefault()), "\"$value\"")
        }
    }

    protected fun DefaultConfig.injectInts(vararg mapping: Pair<String, Int>) {
        mapping.forEach { (key, value) ->
            buildConfigField("int", key.toUpperCase(Locale.getDefault()), "$value")
        }
    }
}
