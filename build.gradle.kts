import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    id("com.github.ben-manes.versions") version Versions.GRADLE_DEPENDENCY_UPDATES
}

buildscript {
    repositories {
        google()
        jcenter()
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.2.0-alpha12")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}")
        classpath("com.github.ben-manes:gradle-versions-plugin:${Versions.GRADLE_DEPENDENCY_UPDATES}")
        classpath("com.google.dagger:hilt-android-gradle-plugin:${Versions.HILT}")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
    }
}

tasks {
    val clean by registering(Delete::class) {
        delete(buildDir)
    }
}

tasks.named<DependencyUpdatesTask>("dependencyUpdates") {
    resolutionStrategy {
        componentSelection {
            all {
                val rejected = listOf("alpha", "beta", "rc", "cr", "m", "preview", "b", "ea").any { qualifier ->
                    candidate.version.matches(Regex("(?i).*[.-]$qualifier[.\\d-+]*"))
                }
                if (rejected) {
                    reject("Release candidate")
                }
            }
        }
    }
    // optional parameters
    checkForGradleUpdate = true
    outputDir = "build/dependencyUpdates"
    reportfileName = "report"
}
