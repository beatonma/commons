package org.beatonma.commons.buildsrc.kts.plugin

import Dependencies
import Modules
import org.gradle.api.plugins.PluginContainer
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.project

class CommonsHiltModule : ProjectPlugin() {

    override fun applyPlugins(plugins: PluginContainer) {
        with(plugins) {
            apply("dagger.hilt.android.plugin")
        }
    }

    override fun applyDependencies(dependencies: DependencyHandlerScope) {
        super.applyDependencies(dependencies)

        with(dependencies) {
            unitTest {
                implementations(
                    Dependencies.Dagger.Hilt.TESTING,
                    project(Modules.TestHilt.toString())
                )
            }

            instrumentationTest {
                annotationProcessors(
                    Dependencies.Dagger.COMPILER,
                    Dependencies.Dagger.ANNOTATION_PROCESSOR
                )

                implementations(
                    Dependencies.Dagger.Hilt.TESTING,
                    project(Modules.TestHilt.toString())
                )
            }

            main {
                annotationProcessors(
                    Dependencies.Dagger.ANNOTATION_PROCESSOR,
                    Dependencies.Dagger.COMPILER,
                    Dependencies.Dagger.Hilt.AX_KAPT,
                    Dependencies.Dagger.Hilt.KAPT
                )

                implementations(
                    Dependencies.Dagger.ANDROID,
                    Dependencies.Dagger.DAGGER,
                    Dependencies.Dagger.SUPPORT,

                    Dependencies.Dagger.Hilt.CORE
                )
            }
        }
    }
}
