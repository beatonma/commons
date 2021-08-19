package org.beatonma.commons.buildsrc.gradle.plugins

import Plugins
import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.beatonma.commons.buildsrc.Git
import org.beatonma.commons.buildsrc.config.Commons
import org.beatonma.commons.buildsrc.gradle.minify
import org.gradle.api.Project
import org.gradle.api.plugins.PluginContainer
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Configure a project as an Android application.
 */
class CommonsApplicationPlugin : CommonsAndroidPlugin<BaseAppModuleExtension> {
    private val timestamp: String =
        SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())

    override fun applyPlugins(plugins: PluginContainer) {
        with(plugins) {
            apply(Plugins.Android.APPLICATION)
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
                    val buildLabel =
                        "$timestamp-${git.commitCount}-${git.sha}-${git.tag}-${git.branch}"
                    val outputName = "commons-release-$buildLabel"

                    applicationVariants.all {
                        outputs
                            .map { it as BaseVariantOutputImpl }
                            .forEach { output ->
                                output.outputFileName = "$outputName.${output.outputFile.extension}"
                            }
                    }

                    target.rootProject.file("proguard").listFiles()
                        ?.filter { it.name.startsWith("proguard") }
                        ?.toTypedArray()
                        ?.let(::proguardFiles)

                    // Make a copy of obfuscation mapping files after release build
                    target.copyMappingFiles("apk/release/$outputName")

                    minify()
                }
            }
        }
    }

    private fun Project.copyMappingFiles(targetDirectory: String) {
        val taskName = "copyMappingFiles"

        afterEvaluate {
            tasks.register(taskName) {
                doLast {
                    val outputDir = rootProject.file(targetDirectory)

                    val success = file("build/outputs/mapping/release/")
                        .copyRecursively(outputDir)

                    if (success) println("Mapping files copied to ${outputDir.absolutePath}")
                    else error(
                        "$taskName failed! Mapping files could not be copied from " +
                            "build/outputs/mapping/release/ to ${outputDir.absolutePath}!"
                    )
                }
            }

            tasks.named("assembleRelease").get().finalizedBy(taskName)
        }
    }
}
