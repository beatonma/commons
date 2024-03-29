pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://plugins.gradle.org/m2/")
    }
}

// Core
include(":core")
include(":test")
include(":test-hilt")
include(":network-core")

// Data: network and persistence
include(":snommoc")
include(":ukparliament")
include(":persistence")
include(":repo")

// UI
include(":theme-core")
include(":test-compose")
include(":compose-core")
include(":app-theme")
include(":svg")

// Debug/dev
include(":sampledata")
include(":theme-preview")

// App
include(":app")
