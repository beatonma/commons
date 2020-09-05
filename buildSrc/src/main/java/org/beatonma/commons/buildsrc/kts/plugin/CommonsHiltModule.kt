package org.beatonma.commons.buildsrc.kts.plugin

import Dependencies
import org.gradle.api.plugins.PluginContainer
import org.gradle.kotlin.dsl.DependencyHandlerScope

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
                    Dependencies.Hilt.TESTING
                )
            }

            main {
                annotationProcessors(
                    Dependencies.Dagger.ANNOTATION_PROCESSOR,
                    Dependencies.Dagger.COMPILER,
                    Dependencies.Hilt.AX_KAPT,
                    Dependencies.Hilt.KAPT
                )

                implementations(
                    Dependencies.Dagger.ANDROID,
                    Dependencies.Dagger.DAGGER,
                    Dependencies.Dagger.SUPPORT,

                    Dependencies.Hilt.CORE
                )
            }
        }
    }
}
