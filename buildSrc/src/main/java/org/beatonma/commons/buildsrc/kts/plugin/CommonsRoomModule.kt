package org.beatonma.commons.buildsrc.kts.plugin

import Dependencies
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class CommonsRoomModule : Plugin<Project> {

    override fun apply(target: Project) {
        with (target) {
            applyDependencies()
        }
    }

    private fun Project.applyDependencies() {
        val annotationProcessors = arrayOf(
            Dependencies.Room.AP
        )

        val implementations = arrayOf(
            Dependencies.Room.KTX,
            Dependencies.Room.RUNTIME
        )

        dependencies {
            annotationProcessors.forEach { "kapt"(it) }
            implementations.forEach { "implementation"(it) }
        }
    }
}
