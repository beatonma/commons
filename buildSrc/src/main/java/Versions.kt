import org.gradle.api.JavaVersion

object Versions {
    val JAVA = JavaVersion.VERSION_1_8
    const val KOTLIN = "1.5.10"
    const val KOTLIN_LANGUAGE_VERSION = "1.5"
    const val COROUTINES = "1.5.1"

    object Build {
        const val GRADLE_PLUGIN = "7.1.0-alpha04"
        const val DEPENDENCY_UPDATES = "0.39.0"
    }

    object Google {
        const val MATERIAL = "1.5.0-alpha01"
        const val DAGGER = "2.38.1"
        const val HILT = DAGGER

        object Play {
            const val AUTH = "19.2.0"
            const val LOCATION = "18.0.0"
        }

        object Maps {
            const val MAPS = "3.1.0-beta"
            const val UTIL = "2.2.5"
            const val KTX = "3.1.0"
        }
    }

    object Jetpack {
        const val HILT = "1.0.0-alpha03"
        const val ACTIVITY = "1.3.0"
        const val ANNOTATIONS = "1.2.0"
        const val APPCOMPAT = "1.4.0-alpha03"
        const val CORE_KTX = "1.7.0-alpha01"
        const val FRAGMENT = "1.4.0-alpha05"
        const val LIFECYCLE = "2.4.0-alpha02"
        const val NAVIGATION = "2.4.0-alpha05"
        const val ROOM = "2.3.0"
        const val SAVED_STATE = "1.1.0"
        const val WORK = "2.4.0"

        object Compose {
            const val COMPOSE = "1.0.0"
            const val ACTIVITY = "1.3.0"
            const val CONSTRAINT_LAYOUT = "1.0.0-beta01"
            const val LIFECYCLE_VIEWMODEL = "1.0.0-alpha07"
            const val NAVIGATION = "2.4.0-alpha05"
        }

        object Test {
            const val CORE = "1.3.0" // 1.3.0 required by hilt 2.38.1
            const val LIVEDATA = "2.1.0"
            const val ESPRESSO = "3.4.0"
        }
    }

    object Square {
        /** Version as used in Retrofit. Check for changes: https://github.com/square/retrofit/blob/master/CHANGELOG.md */
        const val OKHTTP = "3.14.9"
        const val MOSHI = "1.9.2"
        const val RETROFIT = "2.9.0"
        const val LEAK_CANARY = "2.4"
    }

    // Other 3rd party
    const val GLIDE = "4.11.0"
    const val GROUPIE = "2.8.0"
    const val COIL = "1.3.1"
    const val ACCOMPANIST_INSETS = "0.15.0"

    // Testing
    const val MOCKITO = "3.5.10"
    const val MOCKK = "1.10.0"
    const val JUNIT = "4.12"
}
