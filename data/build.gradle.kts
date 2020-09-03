import org.beatonma.commons.buildsrc.Commons
import org.beatonma.commons.buildsrc.Git
import org.beatonma.commons.buildsrc.kts.extensions.coroutines
import org.beatonma.commons.buildsrc.kts.extensions.injectInts
import org.beatonma.commons.buildsrc.kts.extensions.injectStrings

plugins {
    id(Plugins.COMMONS_LIBRARY_CONFIG)
    id(Plugins.COMMONS_HILT_MODULE)
    id(Plugins.COMMONS_ROOM_MODULE)
}

android {
    val git = Git.resolveData(project)

    defaultConfig {
        injectStrings(
            "VERSION_NAME" to git.tag,
            "APPLICATION_ID" to Commons.APPLICATION_ID,
            "GIT_SHA" to git.sha,
            asBuildConfig = true,
            asResValue = false
        )

        injectInts(
            "VERSION_CODE" to git.commitCount,
            asBuildConfig = true,
            asResValue = false
        )

        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
                arg("room.incremental", "true")
                arg("room.expandProjection", "true")
            }
        }
    }
}

dependencies {
    val testAnnotationProcessors = arrayOf(
        Dependencies.Dagger.COMPILER,
        Dependencies.Dagger.ANNOTATION_PROCESSOR
    )
    val testImplementations = arrayOf(
        Dependencies.Test.AndroidX.LIVEDATA
    )

    val implementations = arrayOf(
        Dependencies.AndroidX.CORE_KTX,

        *coroutines,

        project(":core")
    )

    testAnnotationProcessors.forEach(::kaptAndroidTest)
    testImplementations.forEach(::androidTestImplementation)

    implementations.forEach(::implementation)
}
