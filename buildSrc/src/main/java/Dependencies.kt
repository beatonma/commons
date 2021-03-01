import org.gradle.api.JavaVersion


object Versions {
    // Build
    const val GRADLE_PLUGIN = "7.0.0-alpha08"
    const val GRADLE_DEPENDENCY_UPDATES = "0.36.0"

    // Core
    const val COROUTINES = "1.4.2"
    const val KOTLIN = "1.4.31"
    const val KOTLIN_LANGUAGE_VERSION = "1.4"
    val JAVA = JavaVersion.VERSION_1_8

    // Data
    const val ROOM = "2.3.0-alpha02"

    // Dependency injection
    const val DAGGER = "2.33"
    const val HILT = "2.33-beta"
    const val AX_HILT = "1.0.0-alpha03"

    // Google
    const val GP_AUTH = "18.1.0"
    const val GP_LOCATION = "17.0.0"
    const val GP_MAPS = "17.0.0"
    const val G_MAPS = "3.1.0-beta"
    const val G_MAPS_UTIL = "2.0.3"
    const val G_MAPS_KTX = "2.2.0"
    const val MATERIAL = "1.3.0-alpha02"

    // Android X
    const val AX_ACTIVITY = "1.3.0-alpha02"
    const val AX_ANNOTATIONS = "1.2.0-alpha01"
    const val AX_APPCOMPAT = "1.3.0-alpha02"
    const val AX_COMPOSE = "1.0.0-alpha10"
    const val AX_CONSTRAINTLAYOUT = "2.0.4"
    const val AX_CORE_KTX = "1.5.0-beta02"
    const val AX_FRAGMENT = "1.3.0-beta01"
    const val AX_LIFECYCLE = "2.3.0-alpha06"
    const val AX_NAVIGATION = "2.3.1"
    const val AX_RECYCLERVIEW = "1.2.0-alpha05"
    const val AX_WORK = "2.4.0"
    const val AX_VIEWBINDING = "4.1.0-alpha09"

    // Square
    const val OKHTTP =
        "3.14.9"  // Version as used in Retrofit. Check for changes: https://github.com/square/retrofit/blob/master/CHANGELOG.md
    const val MOSHI = "1.9.2"
    const val RETROFIT = "2.9.0"
    const val LEAK_CANARY = "2.4"

    // Other 3rd party
    const val GLIDE = "4.11.0"
    const val GROUPIE = "2.8.0"
    const val COIL = "1.1.0"
    const val CHRISBANES_ACCOMPANIST_COIL = "0.4.0"

    // Testing
    const val AX_TEST_CORE = "1.4.0-alpha04"
    const val AX_TEST_LIVEDATA = "2.1.0"
    const val ESPRESSO = "3.3.0"
    const val MOCKITO = "3.5.10"
    const val MOCKK = "1.10.0"
    const val JUNIT = "4.13"
}

object Dependencies {
    object AndroidX {
        private fun androidx(group: String, artifact: String, version: String) =
            dependency("androidx.$group", artifact, version)

        val APPCOMPAT = androidx("appcompat", "appcompat", Versions.AX_APPCOMPAT)
        val ACTIVITY =
            androidx("activity", "activity-ktx", Versions.AX_ACTIVITY)
        val FRAGMENT = androidx("fragment", "fragment-ktx", Versions.AX_FRAGMENT)
        val CONSTRAINTLAYOUT =
            androidx("constraintlayout", "constraintlayout", Versions.AX_CONSTRAINTLAYOUT)
        val CORE_KTX = androidx("core", "core-ktx", Versions.AX_CORE_KTX)
        val LIFECYCLE_RUNTIME = androidx(
            "lifecycle",
            "lifecycle-runtime-ktx",
            Versions.AX_LIFECYCLE
        )
        val LIVEDATA_KTX = androidx(
            "lifecycle",
            "lifecycle-livedata-ktx",
            Versions.AX_LIFECYCLE
        )
        val VIEWMODEL_KTX = androidx(
            "lifecycle",
            "lifecycle-viewmodel-ktx",
            Versions.AX_LIFECYCLE
        )
        val ANNOTATIONS = androidx("annotation", "annotation", Versions.AX_ANNOTATIONS)
        val RECYCLERVIEW = androidx("recyclerview", "recyclerview", Versions.AX_RECYCLERVIEW)

        val NAVIGATION_FRAGMENT =
            androidx("navigation", "navigation-fragment-ktx", Versions.AX_NAVIGATION)
        val NAVIGATION_UI = androidx("navigation", "navigation-ui-ktx", Versions.AX_NAVIGATION)

        object Compose {
            private fun compose(
                group: String,
                artifact: String = group,
                version: String = Versions.AX_COMPOSE
            ) = androidx("compose.$group", artifact, version)

            val COMPILER = compose("compiler")

            val ANIMATION = compose("animation")

            val FOUNDATION = compose("foundation")
            val FOUNDATION_LAYOUT = compose("foundation-layout")
            val FOUNDATION_TEXT = compose("foundation-text")

            val MATERIAL = compose("material")
            val MATERIAL_ICONS_CORE = compose("material", "material-icons-core")
            val MATERIAL_ICONS_EXTENDED = compose("material", "material-icons-extended")

            val RUNTIME = compose("runtime")
            val LIVEDATA = compose("runtime", "runtime-livedata")

            val TEST = compose("ui", "ui-test")
            val TEST_JUNIT = compose("ui", "ui-test-junit4")

            val TOOLING = compose("ui", "ui-tooling")

            val UI = compose("ui")
        }

        val VIEWBINDING = androidx(
            "databinding",
            "viewbinding",
            Versions.AX_VIEWBINDING
        )

        val WORK = androidx("work", "work-runtime-ktx", Versions.AX_WORK)
    }

    object Build {
        val VERSIONS = dependency(
            "com.github.ben-manes",
            "gradle-versions-plugin",
            Versions.GRADLE_DEPENDENCY_UPDATES
        )
    }

    object Coil {
        val COIL = dependency("io.coil-kt", "coil", Versions.COIL)
        val ACCOMPANIST = dependency(
            "dev.chrisbanes.accompanist",
            "accompanist-coil",
            Versions.CHRISBANES_ACCOMPANIST_COIL
        )
    }

    object Dagger {
        private fun dagger(artifact: String, version: String = Versions.DAGGER) =
            dependency("com.google.dagger", artifact, version)

        val DAGGER = dagger("dagger")
        val ANDROID = dagger("dagger-android")
        val SUPPORT = dagger("dagger-android-support")
        val COMPILER = dagger("dagger-compiler")
        val ANNOTATION_PROCESSOR = dagger("dagger-android-processor")
        val SPI = dagger("dagger-spi")

        object Hilt {

            private fun hilt(artifact: String, version: String = Versions.HILT) =
                dagger(artifact, version)

            private fun hiltAX(artifact: String, version: String = Versions.AX_HILT) =
                dependency("androidx.hilt", artifact, version)

            val LIFECYCLE_VIEWMODEL = hiltAX("hilt-lifecycle-viewmodel")
            val CORE = hilt("hilt-android")
            val WORK = hiltAX("hilt-work")

            val TESTING = hilt("hilt-android-testing")

            val KAPT = hilt("hilt-android-compiler")
            val AX_KAPT = hiltAX("hilt-compiler")
        }
    }

    object Glide {

        private fun glide(artifact: String, version: String = Versions.GLIDE) =
            dependency("com.github.bumptech.glide", artifact, version)

        val COMPILER = glide("compiler")

        val ANNOTATIONS = glide("annotations")
        val CORE = glide("glide")
        val OKHTTP = glide("okhttp3-integration")
        val RECYCLERVIEW = glide("recyclerview-integration")
    }

    object Google {
        private fun gms(artifact: String, version: String) =
            dependency("com.google.android.gms", artifact, version)

        val MATERIAL = dependency("com.google.android.material", "material", Versions.MATERIAL)

        object Play {
            val AUTH = gms("play-services-auth", Versions.GP_AUTH)
            val LOCATION = gms("play-services-location", Versions.GP_LOCATION)
        }

        object Maps {
            val MAPS = gms("play-services-maps", Versions.GP_MAPS)
            val MAPS_KTX = dependency("com.google.maps.android:", "maps-ktx", Versions.G_MAPS_KTX)

            val MAPS_UTIL = dependency(
                "com.google.maps.android",
                "android-maps-utils",
                Versions.G_MAPS_UTIL
            )
            val MAPS_UTIL_KTX = dependency(
                "com.google.maps.android",
                "maps-utils-ktx",
                Versions.G_MAPS_KTX
            )

            object V3 {
                val MAPS = dependency("com.google.android.libraries.maps", "maps", Versions.G_MAPS)
                val MAPS_KTX = dependency("com.google.maps.android", "maps-v3-ktx", Versions.G_MAPS_KTX)

                val MAPS_UTIL = dependency(
                    "com.google.maps.android",
                    "android-maps-utils-v3",
                    Versions.G_MAPS_UTIL
                )
                val MAPS_UTIL_KTX = dependency(
                    "com.google.maps.android",
                    "maps-utils-v3-ktx",
                    Versions.G_MAPS_KTX
                )
            }
        }
    }

    object Kotlin {
        private fun kotlin(artifact: String, version: String = Versions.KOTLIN) =
            dependency("org.jetbrains.kotlin", artifact, version)

        private fun kotlinx(artifact: String, version: String = Versions.KOTLIN) =
            dependency("org.jetbrains.kotlinx", artifact, version)

        val STDLIB = kotlin("kotlin-stdlib-jdk8")
        val REFLECT = kotlin("kotlin-reflect")

        object Coroutines {
            private fun coroutines(artifact: String, version: String = Versions.COROUTINES) =
                kotlinx(
                    "kotlinx-coroutines-$artifact",
                    version
                )

            val CORE = coroutines("core")
            val ANDROID = coroutines("android")
            val PLAY = coroutines("play-services")
            val TEST = coroutines("test")
        }
    }

    object Moshi {
        private fun moshi(artifact: String, version: String = Versions.MOSHI) =
            dependency("com.squareup.moshi", artifact, version)

        val MOSHI = moshi("moshi")
        val KAPT_CODEGEN = moshi("moshi-kotlin-codegen")
    }

    object Retrofit {
        private fun retrofit(artifact: String, version: String = Versions.RETROFIT) =
            dependency("com.squareup.retrofit2", artifact, version)

        val RETROFIT = retrofit("retrofit")

        object Converter {

            val MOSHI = retrofit("converter-moshi")
            val GSON = retrofit("converter-gson")
            val TEXT = retrofit("converter-scalars")
        }

        val MOCK = retrofit("retrofit-mock")
    }

    object Room {
        private fun room(artifact: String, version: String = Versions.ROOM) =
            dependency("androidx.room", artifact, version)

        val AP = room("room-compiler")
        val RUNTIME = room("room-runtime")
        val KTX = room("room-ktx")
        val TEST = room("room-testing")
    }

    object Test {
        object AndroidX {
            val CORE = dependency("androidx.test", "core", Versions.AX_TEST_CORE)
            val RULES =
                dependency("androidx.test", "rules", Versions.AX_TEST_CORE)
            val RUNNER =
                dependency("androidx.test", "runner", Versions.AX_TEST_CORE)
            val LIVEDATA = dependency(
                "androidx.arch.core",
                "core-testing",
                Versions.AX_TEST_LIVEDATA
            )

            object Espresso {
                private fun espresso(artifact: String, version: String = Versions.ESPRESSO) =
                    dependency(
                        "androidx.test.espresso",
                        artifact,
                        version
                    )

                val CORE = espresso("espresso-core")
                val CONTRIB = espresso("espresso-contrib")
                val INTENTS = espresso("espresso-intents")
                val ACCESSIBILITY = espresso("espresso-accessibility")
            }
        }

        val MOCKITO = dependency("org.mockito", "mockito-core", Versions.MOCKITO)
        val MOCKK = dependency("io.mockk", "mockk", Versions.MOCKK)
        val JUNIT = dependency("junit", "junit", Versions.JUNIT)
        val OKHTTP_MOCK_SERVER =
            dependency("com.squareup.okhttp3", "mockwebserver", Versions.OKHTTP)
    }

    object Debug {
        val LEAK_CANARY = dependency(
            "com.squareup.leakcanary",
            "leakcanary-android",
            Versions.LEAK_CANARY
        )
    }
}

private fun dependency(group: String, artifact: String, version: String) =
    "$group:$artifact:$version"
