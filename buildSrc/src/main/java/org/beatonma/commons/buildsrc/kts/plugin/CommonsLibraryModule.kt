package org.beatonma.commons.buildsrc.kts.plugin

import com.android.build.gradle.LibraryExtension
import org.beatonma.commons.buildsrc.kts.extensions.debug
import org.beatonma.commons.buildsrc.kts.extensions.release
import org.gradle.api.Project
import org.gradle.api.plugins.PluginContainer
import java.io.File

/**
 * Project-wide basic configuration for library modules.
 */
class CommonsLibraryModule : CommonsAndroidModule<LibraryExtension>() {

    override fun applyPlugins(plugins: PluginContainer) {
        with(plugins) {
            apply("com.android.library")
        }
        super.applyPlugins(plugins)
    }

    override fun applyAndroidConfig(android: LibraryExtension, target: Project) {
        super.applyAndroidConfig(android, target)
        with(android) {
            defaultConfig {
                consumerProguardFiles.add(File("consumer-rules.pro"))
            }

            buildTypes {
                debug {
                    isDebuggable = true
                }

                release {
                    isDebuggable = false
                }
            }
        }
    }
}
