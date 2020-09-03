package org.beatonma.commons.buildsrc.kts.plugin

import Dependencies
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class CommonsHiltModule : Plugin<Project> {

    override fun apply(target: Project) {
        with (target) {
            applyPlugins()
            applyDependencies()
        }
    }

    private fun Project.applyPlugins() {
        plugins.run {
            apply("dagger.hilt.android.plugin")
        }
    }

    private fun Project.applyDependencies() {
        val testImplementations = arrayOf(
            Dependencies.Hilt.TESTING
        )

        val annotationProcessors = arrayOf(
            Dependencies.Dagger.ANNOTATION_PROCESSOR,
            Dependencies.Dagger.COMPILER,
            Dependencies.Hilt.AX_KAPT,
            Dependencies.Hilt.KAPT
        )

        val implementations = arrayOf(
            Dependencies.Dagger.ANDROID,
            Dependencies.Dagger.DAGGER,
            Dependencies.Dagger.SUPPORT,

            Dependencies.Hilt.CORE
        )

        dependencies {
            testImplementations.forEach { "testImplementation"(it) }
            annotationProcessors.forEach { "kapt"(it) }
            implementations.forEach { "implementation"(it) }
        }
    }
}
