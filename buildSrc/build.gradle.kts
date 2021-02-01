import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val gradleVersion = "7.0.0-alpha04"
val kotlinVersion = "1.4.21"

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
    languageVersion = "1.4"
}
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
    languageVersion = "1.4"
}

gradlePlugin {
    plugins {
        register("commons-application-module") {
            id = "commons-application-module"
            implementationClass = "org.beatonma.commons.buildsrc.kts.plugin.CommonsApplicationModule"
        }

        register("commons-library-module") {
            id = "commons-library-module"
            implementationClass = "org.beatonma.commons.buildsrc.kts.plugin.CommonsLibraryModule"
        }

        register("commons-hilt-module") {
            id = "commons-hilt-module"
            implementationClass = "org.beatonma.commons.buildsrc.kts.plugin.CommonsHiltModule"
        }

        register("commons-room-module") {
            id = "commons-room-module"
            implementationClass = "org.beatonma.commons.buildsrc.kts.plugin.CommonsRoomModule"
        }

        register("commons-compose-module") {
            id = "commons-compose-module"
            implementationClass = "org.beatonma.commons.buildsrc.kts.plugin.CommonsComposeModule"
        }
    }
}
