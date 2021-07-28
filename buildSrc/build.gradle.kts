import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val gradleVersion = "7.1.0-alpha05"
val kotlinVersion = "1.5.10"

plugins {
    `kotlin-dsl`
}

dependencies {
    implementation("com.android.tools.build:gradle:$gradleVersion")
    implementation(kotlin("gradle-plugin", kotlinVersion))
}

repositories {
    google()
    jcenter()
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
}

val compileKotlin: KotlinCompile by tasks
val compileTestKotlin: KotlinCompile by tasks

compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
    languageVersion = "1.5"
}
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
    languageVersion = "1.5"
}

gradlePlugin {
    plugins {
        register("commons-application-module") {
            id = "commons-application-module"
            implementationClass =
                "org.beatonma.commons.buildsrc.gradle.plugins.CommonsApplicationModule"
        }

        register("commons-library-module") {
            id = "commons-library-module"
            implementationClass =
                "org.beatonma.commons.buildsrc.gradle.plugins.CommonsLibraryModule"
        }

        register("commons-hilt-module") {
            id = "commons-hilt-module"
            implementationClass = "org.beatonma.commons.buildsrc.gradle.plugins.CommonsHiltModule"
        }

        register("commons-room-module") {
            id = "commons-room-module"
            implementationClass = "org.beatonma.commons.buildsrc.gradle.plugins.CommonsRoomModule"
        }

        register("commons-compose-module") {
            id = "commons-compose-module"
            implementationClass =
                "org.beatonma.commons.buildsrc.gradle.plugins.CommonsComposeModule"
        }
    }
}
