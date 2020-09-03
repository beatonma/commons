package org.beatonma.commons.buildsrc.kts.plugin

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Project
import java.io.File

/**
 * Project-wide basic configuration for library modules.
 */
class CommonsLibraryModule: CommonsAndroidModule<LibraryExtension>() {
    override fun applyPlugins(target: Project) {
        target.plugins.run {
            apply("com.android.library")
            apply("kotlin-android")
            apply("kotlin-android-extensions")
            apply("kotlin-kapt")
            apply("com.github.ben-manes.versions")
        }
//        super.applyPlugins(target)
    }

    override fun applyAndroidConfig(target: Project) {
        super.applyAndroidConfig(target)
        target.android.run {
            defaultConfig {
                consumerProguardFiles.add(File("consumer-rules.pro"))
            }
        }
    }
}
