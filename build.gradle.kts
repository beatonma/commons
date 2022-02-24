plugins {
    id("com.github.ben-manes.versions") version Versions.Build.DEPENDENCY_UPDATES
}

buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    dependencies {
        classpath(Dependencies.Build.GRADLE)
        classpath(Dependencies.Build.KOTLIN)
        classpath(Dependencies.Build.HILT)
        classpath(Dependencies.Build.VERSIONS)
        classpath("com.squareup:javapoet:1.13.0")  // Fix for Dagger 2.41
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter() // Required for Volley 1.1.1 (required by Google Maps)
    }
}

tasks {
    val clean by registering(Delete::class) {
        delete(buildDir)
    }

    named(
        "dependencyUpdates",
        com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask::class
    ) {
        resolutionStrategy {
            componentSelection {
                all {
                    val rejected = listOf(
                        "alpha",
                        "beta",
                        "rc",
                        "cr",
                        "m",
                        "preview",
                        "b",
                        "ea"
                    ).any { qualifier ->
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
        outputFormatter = "json"
    }
}
