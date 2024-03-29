package org.beatonma.commons.buildsrc.gradle.plugins

import Dependencies
import Modules
import Plugins
import org.beatonma.commons.buildsrc.gradle.project
import org.gradle.api.plugins.PluginContainer
import org.gradle.kotlin.dsl.DependencyHandlerScope

/**
 * Configure a project for use with Hilt.
 */
class CommonsHiltPlugin : ProjectPlugin {

    override fun applyPlugins(plugins: PluginContainer) {
        with(plugins) {
            apply(Plugins.HILT)
        }
        super.applyPlugins(plugins)
    }

    override fun applyDependencies(dependencies: DependencyHandlerScope) {
        super.applyDependencies(dependencies)

        with(dependencies) {
            unitTest {
                implementations(
                    Dependencies.Dagger.Hilt.TESTING,
                    project(Modules.TestHilt)
                )
            }

            instrumentationTest {
                implementations(
                    Dependencies.Dagger.Hilt.TESTING,
                    project(Modules.TestHilt)
                )
            }

            main {
                annotationProcessors(
                    Dependencies.Dagger.AP_ANDROID,
                    Dependencies.Dagger.AP_COMPILER,
                    Dependencies.Dagger.Hilt.AP,
                    Dependencies.Dagger.Hilt.KAPT_JETPACK,
                )

                implementations(
                    Dependencies.Dagger.ANDROID,
                    Dependencies.Dagger.DAGGER,
                    Dependencies.Dagger.ANDROID_SUPPORT,
                    Dependencies.Dagger.Hilt.HILT
                )
            }
        }
    }
}
