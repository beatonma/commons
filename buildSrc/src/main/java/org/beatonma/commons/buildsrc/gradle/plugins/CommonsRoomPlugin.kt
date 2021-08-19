package org.beatonma.commons.buildsrc.gradle.plugins

import Dependencies
import org.gradle.kotlin.dsl.DependencyHandlerScope

/**
 * Configure a project for use with Room.
 */
class CommonsRoomPlugin : ProjectPlugin {

    override fun applyDependencies(dependencies: DependencyHandlerScope) {
        super.applyDependencies(dependencies)

        with(dependencies) {
            main {
                annotationProcessors(
                    Dependencies.Room.AP
                )

                implementations(
                    Dependencies.Room.KTX,
                    Dependencies.Room.RUNTIME,
                )
            }
        }
    }
}
