pluginManagement {
    repositories {
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
    }
}
include(":core")
include(":snommoc")
include(":data")
include(":app")
include(":bmalib:testing")
