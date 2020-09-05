import org.gradle.api.JavaVersion


object Versions {
    // Core
    const val COROUTINES = "1.3.9"
    const val KOTLIN = "1.4.0"
    val JAVA = JavaVersion.VERSION_1_8

    // Data
    const val ROOM = "2.3.0-alpha02"

    // Dependency injection
    const val DAGGER = "2.28.3"
    const val HILT = "2.28.3-alpha"
    const val AX_HILT = "1.0.0-alpha02"

    // Google
    const val GP_AUTH = "18.1.0"
    const val GP_LOCATION = "17.0.0"
    const val GP_MAPS = "17.0.0"
    const val GP_MAPS_UTIL = "0.1"
    const val MATERIAL = "1.3.0-alpha02"

    // Android X
    const val AX_ANNOTATIONS = "1.2.0-alpha01"
    const val AX_APPCOMPAT = "1.3.0-alpha02"
    const val AX_COMPOSE = "1.0.0-alpha01"
    const val AX_CONSTRAINTLAYOUT = "2.0.1"
    const val AX_CORE_KTX = "1.5.0-alpha02"
    const val AX_LIFECYCLE = "2.3.0-alpha06"
    const val AX_NAVIGATION = "2.3.0"
    const val AX_RECYCLERVIEW = "1.2.0-alpha05"
    const val AX_SWIPEREFRESH = "1.1.0-rc01"
    const val AX_WORK = "2.4.0"
    const val AX_VIEWBINDING = "4.1.0-alpha09"

    // Square
    const val OKHTTP = "3.14.9"  // Version as used in Retrofit
    const val MOSHI = "1.9.2"
    const val RETROFIT = "2.9.0"
    const val LEAK_CANARY = "2.4"

    // Other 3rd party
    const val GLIDE = "4.11.0"
    const val GROUPIE = "2.8.0"

    // Testing
    const val AX_TEST_CORE = "1.3.0"
    const val AX_TEST_LIVEDATA = "2.1.0"
    const val ESPRESSO = "3.3.0"
    const val MOCKITO = "3.5.10"
    const val MOCKK = "1.10.0"
    const val JUNIT = "4.13"

    // Build
    const val GRADLE_DEPENDENCY_UPDATES = "0.29.0"
}

object Dependencies {
    object AndroidX {
        val APPCOMPAT = androidx("appcompat", version = Versions.AX_APPCOMPAT)
        val CONSTRAINTLAYOUT = androidx("constraintlayout", version = Versions.AX_CONSTRAINTLAYOUT)
        val CORE_KTX =
            androidx(group = "core", artifact = "core-ktx", version = Versions.AX_CORE_KTX)
        val LIFECYCLE_RUNTIME = androidx(
            group = "lifecycle",
            artifact = "lifecycle-runtime-ktx",
            version = Versions.AX_LIFECYCLE
        )
        val LIVEDATA_KTX = androidx(
            group = "lifecycle",
            artifact = "lifecycle-livedata-ktx",
            version = Versions.AX_LIFECYCLE
        )
        val VIEWMODEL_KTX = androidx(
            group = "lifecycle",
            artifact = "lifecycle-viewmodel-ktx",
            version = Versions.AX_LIFECYCLE
        )
        val ANNOTATIONS = androidx("annotation", version = Versions.AX_ANNOTATIONS)
        val RECYCLERVIEW = androidx(artifact = "recyclerview", version = Versions.AX_RECYCLERVIEW)
        val SWIPE_REFRESH_LAYOUT =
            androidx(artifact = "swiperefreshlayout", version = Versions.AX_SWIPEREFRESH)

        val NAVIGATION_FRAGMENT = androidx(
            group = "navigation",
            artifact = "navigation-fragment-ktx",
            version = Versions.AX_NAVIGATION
        )
        val NAVIGATION_UI = androidx(
            group = "navigation",
            artifact = "navigation-ui-ktx",
            version = Versions.AX_NAVIGATION
        )

        object Compose {

            private fun compose(
                group: String,
                artifact: String = group,
                version: String = Versions.AX_COMPOSE
            ) = androidx(group = "compose.$group", artifact = artifact, version = version)

            val core
                get() = arrayOf(
                    COMPOSE,
                    FOUNDATION
                )

            val COMPOSE = compose("ui", "ui")
            val FOUNDATION = compose("foundation")
            val MATERIAL = compose("material")

            object MaterialIcons {

                val CORE = compose("material", "material-icons-core")
                val EXTENDED = compose("material", "material-icons-extended")
            }

            val LIVEDATA = compose("runtime", "runtime-livedata")

            val TEST = androidx(group = "ui", artifact = "ui-test", version = Versions.AX_COMPOSE)

            val TOOLING =
                androidx(group = "ui", artifact = "ui-tooling", version = Versions.AX_COMPOSE)
        }

        val VIEWBINDING = androidx(
            group = "databinding",
            artifact = "viewbinding",
            version = Versions.AX_VIEWBINDING
        )

        val WORK =
            androidx(group = "work", artifact = "work-runtime-ktx", version = Versions.AX_WORK)
    }

    object Build {
        val VERSIONS = dependency(
            "com.github.ben-manes",
            "gradle-versions-plugin",
            Versions.GRADLE_DEPENDENCY_UPDATES
        )
    }

    object Dagger {
        val DAGGER = dagger("dagger")
        val ANDROID = dagger("dagger-android")
        val SUPPORT = dagger("dagger-android-support")
        val COMPILER = dagger("dagger-compiler")
        val ANNOTATION_PROCESSOR = dagger("dagger-android-processor")
        val SPI = dagger("dagger-spi")
    }

    object Hilt {
        val LIFECYCLE_VIEWMODEL = hiltAX("hilt-lifecycle-viewmodel")
        val CORE = hilt("hilt-android")
        val WORK = hiltAX("hilt-work")

        val TESTING = hilt("hilt-android-testing")

        val KAPT = hilt("hilt-android-compiler")
        val AX_KAPT = hiltAX("hilt-compiler")
    }

    object Glide {
        val COMPILER = glide("compiler")

        val ANNOTATIONS = glide("annotations")
        val CORE = glide("glide")
        val OKHTTP = glide("okhttp3-integration")
        val RECYCLERVIEW = glide("recyclerview-integration")
    }

    object Google {
        val MATERIAL = dependency("com.google.android.material", "material", Versions.MATERIAL)

        object Play {
            val AUTH = gms("play-services-auth", Versions.GP_AUTH)
            val LOCATION = gms("play-services-location", Versions.GP_LOCATION)
            val MAPS = gms("play-services-maps", Versions.GP_MAPS)
            val MAPS_UTIL = dependency(
                "com.google.maps.android",
                artifact = "android-maps-utils-sdk-v3-compat",
                version = Versions.GP_MAPS_UTIL
            )
        }
    }

    object Groupie {
        val CORE = dependency("com.xwray", artifact = "groupie", version = Versions.GROUPIE)
        val KTX = dependency(
            "com.xwray",
            artifact = "groupie-kotlin-android-extensions",
            version = Versions.GROUPIE
        )
        val VIEWBINDING =
            dependency("com.xwray", artifact = "groupie-viewbinding", version = Versions.GROUPIE)
    }

    object Kotlin {
        val STDLIB = kotlin("kotlin-stdlib-jdk8")
        val REFLECT = kotlin("kotlin-reflect")

        object Coroutines {
            val CORE = kotlinx(
                "kotlinx-coroutines-core",
                Versions.COROUTINES
            )
            val ANDROID = kotlinx(
                "kotlinx-coroutines-android",
                Versions.COROUTINES
            )
            val PLAY = kotlinx(
                "kotlinx-coroutines-play-services",
                Versions.COROUTINES
            )
            val TEST = kotlinx(
                "kotlinx-coroutines-test",
                Versions.COROUTINES
            )
        }
    }

    object Moshi {
        val MOSHI = moshi("moshi")
        val KAPT_CODEGEN = moshi("moshi-kotlin-codegen")
    }

    object Retrofit {
        val RETROFIT = retrofit("retrofit")

        object Converter {
            val MOSHI = retrofit("converter-moshi")
            val GSON = retrofit("converter-gson")
            val TEXT = retrofit("converter-scalars")
        }
    }

    object Room {
        val AP = room("room-compiler")
        val RUNTIME = room("room-runtime")
        val KTX = room("room-ktx")
    }

    object Test {
        object AndroidX {
            val CORE = androidx(group = "test", artifact = "core", version = Versions.AX_TEST_CORE)
            val RULES =
                androidx(group = "test", artifact = "rules", version = Versions.AX_TEST_CORE)
            val RUNNER =
                androidx(group = "test", artifact = "runner", version = Versions.AX_TEST_CORE)
            val LIVEDATA = androidx(
                group = "arch.core",
                artifact = "core-testing",
                version = Versions.AX_TEST_LIVEDATA
            )
            object Espresso {
                val CORE = espresso("espresso-core")
                val CONTRIB = espresso("espresso-contrib")
                val INTENTS = espresso("espresso-intents")
                val ACCESSIBILITY = espresso("espresso-accessibility")
            }
        }

        val MOCKITO = dependency("org.mockito", "mockito-core", Versions.MOCKITO)
        val MOCKK = dependency("io.mockk", "mockk", Versions.MOCKK)
        val JUNIT = dependency("junit", "junit", Versions.JUNIT)
        val RETROFIT_MOCK = retrofit("retrofit-mock")
        val ROOM = room("room-testing")
        val OKHTTP_MOCK_SERVER =
            square("okhttp3", "mockwebserver", Versions.OKHTTP)
    }

    object Debug {
        val LEAK_CANARY = square(
            group = "leakcanary",
            artifact = "leakcanary-android",
            version = Versions.LEAK_CANARY
        )
    }
}



private fun dependency(group: String, artifact: String, version: String) = "$group:$artifact:$version"
private fun androidx(artifact: String, version: String, group: String = artifact) =
    dependency("androidx.$group", artifact, version)

private fun dagger(artifact: String, version: String = Versions.DAGGER) =
    dependency("com.google.dagger", artifact, version)

private fun hilt(artifact: String, version: String = Versions.HILT) =
    dagger(artifact = artifact, version = version)
private fun hiltAX(artifact: String, version: String = Versions.AX_HILT) =
    androidx(group = "hilt", artifact = artifact, version = version)

private fun espresso(artifact: String, version: String = Versions.ESPRESSO) =
    androidx(
        group = "test.espresso",
        artifact = artifact,
        version = version
    )

private fun glide(artifact: String, version: String = Versions.GLIDE) =
    dependency("com.github.bumptech.glide", artifact, version)

private fun gms(artifact: String, version: String) =
    dependency(
        group = "com.google.android.gms",
        artifact = artifact,
        version = version
    )

private fun kotlin(artifact: String, version: String = Versions.KOTLIN) =
    dependency(group = "org.jetbrains.kotlin", artifact = artifact, version = version)

private fun kotlinx(artifact: String, version: String = Versions.KOTLIN) =
    dependency(
        group = "org.jetbrains.kotlinx",
        artifact = artifact,
        version = version
    )

private fun room(artifact: String, version: String = Versions.ROOM) =
    androidx(group = "room", artifact = artifact, version = version)

private fun square(group: String, artifact: String, version: String) =
    dependency("com.squareup.$group", artifact, version)

private fun retrofit(artifact: String, version: String = Versions.RETROFIT) =
    square("retrofit2", artifact, version)

private fun moshi(artifact: String, version: String = Versions.MOSHI) =
    square("moshi", artifact, version)
