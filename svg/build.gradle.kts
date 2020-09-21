import org.beatonma.commons.buildsrc.kts.extensions.main

plugins {
    id(Plugins.COMMONS_LIBRARY_CONFIG)
}

android {
    kotlinOptions {
        useIR = true
        jvmTarget = Versions.JAVA.toString()
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-Xallow-jvm-ir-dependencies",
            "-Xskip-prerelease-check"
        )
    }
}

dependencies {
    main {
        implementations(
            Dependencies.AndroidX.Compose.UI
        )
    }
}
