object Plugins {
    object Commons {
        /**
         * [org.beatonma.commons.buildsrc.gradle.plugins.CommonsApplicationPlugin]
         * Apply standard plugins, android configuration block, repositories and basic dependencies.
         */
        const val COMMONS_APPLICATION_CONFIG = "commons-application-plugin"

        /**
         * [org.beatonma.commons.buildsrc.gradle.plugins.CommonsLibraryPlugin]
         * Apply standard plugins, android configuration block, repositories and basic dependencies.
         */
        const val COMMONS_LIBRARY_CONFIG = "commons-library-plugin"

        /**
         * Apply required Dagger/Hilt plugins and dependencies.
         * [org.beatonma.commons.buildsrc.gradle.plugins.CommonsHiltPlugin]
         */
        const val COMMONS_HILT_MODULE = "commons-hilt-plugin"

        /**
         * Apply required Room dependencies.
         * [org.beatonma.commons.buildsrc.gradle.plugins.CommonsRoomPlugin]
         */
        const val COMMONS_ROOM_MODULE = "commons-room-plugin"

        /**
         * Apply required Jetpack Compose dependencies and android configuration block.
         * [org.beatonma.commons.buildsrc.gradle.plugins.CommonsComposePlugin]
         */
        const val COMMONS_COMPOSE_MODULE = "commons-compose-plugin"
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
