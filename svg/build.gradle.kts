import org.beatonma.commons.buildsrc.gradle.main

plugins {
    id(Plugins.Commons.COMMONS_LIBRARY_CONFIG)
}

android {
    kotlinOptions {
        useIR = true
        jvmTarget = Versions.JAVA.toString()
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-Xskip-prerelease-check"
        )
    }
}

dependencies {
    main {
        implementations(
            Dependencies.Jetpack.Compose.UI
        )
    }
}
