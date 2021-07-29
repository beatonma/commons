object Plugins {
    object Commons {
        /**
         * [org.beatonma.commons.buildsrc.gradle.plugins.CommonsApplicationModule]
         * Apply standard plugins, android configuration block, repositories and basic dependencies.
         */
        const val COMMONS_APPLICATION_CONFIG = "commons-application-module"

        /**
         * [org.beatonma.commons.buildsrc.gradle.plugins.CommonsLibraryModule]
         * Apply standard plugins, android configuration block, repositories and basic dependencies.
         */
        const val COMMONS_LIBRARY_CONFIG = "commons-library-module"

        /**
         * Apply required Dagger/Hilt plugins and dependencies.
         * [org.beatonma.commons.buildsrc.gradle.plugins.CommonsHiltModule]
         */
        const val COMMONS_HILT_MODULE = "commons-hilt-module"

        /**
         * Apply required Room dependencies.
         * [org.beatonma.commons.buildsrc.gradle.plugins.CommonsRoomModule]
         */
        const val COMMONS_ROOM_MODULE = "commons-room-module"

        /**
         * Apply required Jetpack Compose dependencies and android configuration block.
         * [org.beatonma.commons.buildsrc.gradle.plugins.CommonsComposeModule]
         */
        const val COMMONS_COMPOSE_MODULE = "commons-compose-module"
    }

    object Android {
        const val LIBRARY = "com.android.library"
        const val APPLICATION = "com.android.application"
    }

    object Kotlin {
        const val KAPT = "kotlin-kapt"
        const val ANDROID = "kotlin-android"
        const val PARCELIZE = "kotlin-parcelize"
    }

    const val HILT = "dagger.hilt.android.plugin"
    const val VERSIONS = "com.github.ben-manes.versions"
}
