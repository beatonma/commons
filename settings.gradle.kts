pluginManagement {
    repositories {
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
    }
}
include(":core")
include(":network-core")
include(":snommoc")
include(":persistence")
include(":repo")
include(":app")
include(":test")
include(":theme")
include(":compose")
include(":themepreview")
include(":svg")
include(":testcompose")
include(":ukparliament")
