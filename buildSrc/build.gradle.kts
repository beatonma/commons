import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// Versions object is not accessible here.
val gradleVersion = "7.1.2"
val kotlinVersion = "1.6.10"
val kotlinLanguageVersion = "1.6"
val javaVersion = "1.8"

plugins {
    `kotlin-dsl`
}

dependencies {
    implementation("com.android.tools.build:gradle:$gradleVersion")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")

    implementation("com.squareup:javapoet:1.13.0")  // Fix for Dagger 2.41
}

repositories {
    google()
    gradlePluginPortal()
    mavenCentral()
}

val compileKotlin: KotlinCompile by tasks
val compileTestKotlin: KotlinCompile by tasks

compileKotlin.kotlinOptions {
    jvmTarget = javaVersion
    languageVersion = kotlinLanguageVersion
}
compileTestKotlin.kotlinOptions {
    jvmTarget = javaVersion
    languageVersion = kotlinLanguageVersion
}

gradlePlugin {
    plugins {
        register("commons-application-plugin") {
            id = "commons-application-plugin"
            implementationClass =
                "org.beatonma.commons.buildsrc.gradle.plugins.CommonsApplicationPlugin"
        }

        register("commons-library-plugin") {
            id = "commons-library-plugin"
            implementationClass =
                "org.beatonma.commons.buildsrc.gradle.plugins.CommonsLibraryPlugin"

        }

        register("commons-hilt-plugin") {
            id = "commons-hilt-plugin"
            implementationClass = "org.beatonma.commons.buildsrc.gradle.plugins.CommonsHiltPlugin"
        }

        register("commons-room-plugin") {
            id = "commons-room-plugin"
            implementationClass = "org.beatonma.commons.buildsrc.gradle.plugins.CommonsRoomPlugin"
        }

        register("commons-compose-plugin") {
            id = "commons-compose-plugin"
            implementationClass =
                "org.beatonma.commons.buildsrc.gradle.plugins.CommonsComposePlugin"
        }
    }
}
