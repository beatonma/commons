pluginManagement {
    repositories {
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
    }
}
include (
    ":bmalib:graphic-core",
    ":bmalib:paintedview",
    ":bmalib:recyclerview",
    ":bmalib:util",
    ":bmalib:style",
    ":bmalib:testing"
)
include(":app")
