package org.beatonma.commons.buildsrc.kts.plugin

import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.beatonma.commons.buildsrc.Commons
import org.beatonma.commons.buildsrc.Git
import org.beatonma.commons.buildsrc.kts.extensions.debug
import org.beatonma.commons.buildsrc.kts.extensions.minify
import org.beatonma.commons.buildsrc.kts.extensions.release
import org.gradle.api.Project
import org.gradle.api.plugins.PluginContainer

class CommonsApplicationModule: CommonsAndroidModule<BaseAppModuleExtension>() {

    override fun applyPlugins(plugins: PluginContainer) {
        with(plugins) {
            apply("com.android.application")
        }
        super.applyPlugins(plugins)
    }

    override fun applyAndroidConfig(android: BaseAppModuleExtension, target: Project) {
        super.applyAndroidConfig(android, target)

        val git = Git.resolveData(target)

        with(android) {
            defaultConfig {
                applicationId = Commons.APPLICATION_ID
            }

            buildTypes {
                debug {
                    isDebuggable = true
                    applicationIdSuffix = ".debug"
                    versionNameSuffix = "-dev"

                    minify(false)
                }

                release {
                    applicationVariants.all {
                        outputs
                            .map { it as BaseVariantOutputImpl }
                            .forEach { output ->
                                output.outputFileName = output.outputFileName
                                    .replace("app-", "commons-")
                                    .replace(
                                        "-release",
                                        "-release-$timestamp-${git.commitCount}-${git.tag}-${git.sha}"
                                    )
                            }
                    }

                    target.rootProject.file("proguard").listFiles()
                        ?.filter { it.name.startsWith("proguard") }
                        ?.toTypedArray()
                        ?.let { proguardFiles(*it) }

                    minify()
                }
            }

            applyPackagingOptions()
        }
    }

    private fun BaseAppModuleExtension.applyPackagingOptions() {
        val exclusions = arrayOf(
            "META-INF/DEPENDENCIES",
            "META-INF/LICENSE",
            "META-INF/LICENSE.txt",
            "META-INF/license.txt",
            "META-INF/NOTICE",
            "META-INF/NOTICE.txt",
            "META-INF/notice.txt",
            "META-INF/ASL2.0",
            "META-INF/AL2.0",
            "META-INF/LGPL2.1",
            "META-INF/*.kotlin_module",
            "META-INF/licenses/*",
            "**/attach_hotspot_windows.dll"
        )

        packagingOptions {
            exclusions.forEach(::exclude)
        }
    }
}
