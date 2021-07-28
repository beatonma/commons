package org.beatonma.commons.buildsrc.gradle.plugins

import Dependencies
import org.gradle.kotlin.dsl.DependencyHandlerScope

class CommonsRoomModule : ProjectPlugin() {

    override fun applyDependencies(dependencies: DependencyHandlerScope) {
        super.applyDependencies(dependencies)

        with(dependencies) {
            main {
                annotationProcessors(
                    Dependencies.Room.AP
                )

                implementations(
                    Dependencies.Room.KTX,
                    Dependencies.Room.RUNTIME
                )
            }
        }
    }
}
