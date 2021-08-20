object Dependencies {

    object Jetpack {
        val APPCOMPAT = dependency("androidx.appcompat:appcompat", Versions.Jetpack.APPCOMPAT)
        val ACTIVITY = dependency("androidx.activity:activity-ktx", Versions.Jetpack.ACTIVITY)
        val ACTIVITY_COMPOSE =
            dependency("androidx.activity:activity-compose", Versions.Jetpack.Compose.ACTIVITY)
        val FRAGMENT = dependency("androidx.fragment:fragment-ktx", Versions.Jetpack.FRAGMENT)

        val CONSTRAINTLAYOUT_COMPOSE = dependency(
            "androidx.constraintlayout:constraintlayout-compose",
            Versions.Jetpack.Compose.CONSTRAINT_LAYOUT
        )
        val CORE_KTX = dependency("androidx.core:core-ktx", Versions.Jetpack.CORE_KTX)
        val LIFECYCLE_RUNTIME =
            dependency("androidx.lifecycle:lifecycle-runtime-ktx", Versions.Jetpack.LIFECYCLE)
        val LIFECYCLE_VIEWMODEL_COMPOSE = dependency(
            "androidx.lifecycle:lifecycle-viewmodel-compose",
            Versions.Jetpack.Compose.LIFECYCLE_VIEWMODEL
        )
        val LIVEDATA_KTX =
            dependency("androidx.lifecycle:lifecycle-livedata-ktx", Versions.Jetpack.LIFECYCLE)
        val VIEWMODEL_KTX =
            dependency("androidx.lifecycle:lifecycle-viewmodel-ktx", Versions.Jetpack.LIFECYCLE)
        val SAVEDSTATE =
            dependency("androidx.savedstate:savedstate-ktx", Versions.Jetpack.SAVED_STATE)
        val ANNOTATIONS = dependency("androidx.annotation:annotation", Versions.Jetpack.ANNOTATIONS)

        val NAVIGATION_FRAGMENT =
            dependency("androidx.navigation:navigation-fragment-ktx", Versions.Jetpack.NAVIGATION)
        val NAVIGATION_UI =
            dependency("androidx.navigation:navigation-ui-ktx", Versions.Jetpack.NAVIGATION)
        val NAVIGATION_COMPOSE = dependency(
            "androidx.navigation:navigation-compose",
            Versions.Jetpack.Compose.NAVIGATION
        )

        object Compose {
            private fun compose(name: String) = dependency(name, Versions.Jetpack.Compose.COMPOSE)

            val COMPILER = compose("androidx.compose.compiler:compiler")
            val ANIMATION = compose("androidx.compose.animation:animation")
            val FOUNDATION = compose("androidx.compose.foundation:foundation")

            val MATERIAL = compose("androidx.compose.material:material")
            val MATERIAL_ICONS_CORE = compose("androidx.compose.material:material-icons-core")
            val MATERIAL_ICONS_EXTENDED =
                compose("androidx.compose.material:material-icons-extended")


            val RUNTIME = compose("androidx.compose.runtime:runtime")
            val LIVEDATA = compose("androidx.compose.runtime:runtime-livedata")

            val TEST = compose("androidx.compose.ui:ui-test")
            val TEST_JUNIT = compose("androidx.compose.ui:ui-test-junit4")
            val TEST_MANIFEST = compose("androidx.compose.ui:ui-test-manifest")

            val TOOLING = compose("androidx.compose.ui:ui-tooling")
            val TOOLING_PREVIEW = compose("androidx.compose.ui:ui-tooling-preview")
            val UI = compose("androidx.compose.ui:ui")
        }

        val WORK = dependency("androidx.work:work-runtime-ktx", Versions.Jetpack.WORK)
    }

    object Build {
        val GRADLE = dependency("com.android.tools.build:gradle", Versions.Build.GRADLE_PLUGIN)
        val KOTLIN = dependency("org.jetbrains.kotlin:kotlin-gradle-plugin", Versions.KOTLIN)
        val HILT = dependency("com.google.dagger:hilt-android-gradle-plugin", Versions.Google.HILT)
        val VERSIONS = dependency(
            "com.github.ben-manes:gradle-versions-plugin",
            Versions.Build.DEPENDENCY_UPDATES
        )
    }

    object Coil {
        val COIL = dependency("io.coil-kt:coil", Versions.COIL)
        val COIL_COMPOSE = dependency("io.coil-kt:coil-compose", Versions.COIL)
    }

    object Accompanist {
        val INSETS =
            dependency("com.google.accompanist:accompanist-insets", Versions.ACCOMPANIST_INSETS)
    }

    object Dagger {
        private fun dagger(name: String) = dependency(name, Versions.Google.DAGGER)

        val DAGGER = dagger("com.google.dagger:dagger")
        val ANDROID = dagger("com.google.dagger:dagger-android")
        val ANDROID_SUPPORT = dagger("com.google.dagger:dagger-android-support")
        val AP_COMPILER = dagger("com.google.dagger:dagger-compiler")
        val AP_ANDROID = dagger("com.google.dagger:dagger-android-processor")
        val SPI = dagger("com.google.dagger:dagger-spi")

        object Hilt {
            private fun hilt(name: String) = dependency(name, Versions.Google.HILT)
            private fun hiltJetpack(name: String) = dependency(name, Versions.Jetpack.HILT)

            val AP = hilt("com.google.dagger:hilt-android-compiler")
            val HILT = hilt("com.google.dagger:hilt-android")
            val TESTING = hilt("com.google.dagger:hilt-android-testing")

            val KAPT_JETPACK = hiltJetpack("androidx.hilt:hilt-compiler")
            val LIFECYCLE_VIEWMODEL = hiltJetpack("androidx.hilt:hilt-lifecycle-viewmodel")
            val NAV_COMPOSE = hiltJetpack("androidx.hilt:hilt-navigation-compose")
            val WORK = hiltJetpack("androidx.hilt:hilt-work")

        }
    }

    object Google {
        val MATERIAL = dependency("com.google.android.material:material", Versions.Google.MATERIAL)

        object Play {
            val AUTH =
                dependency("com.google.android.gms:play-services-auth", Versions.Google.Play.AUTH)
            val LOCATION = dependency(
                "com.google.android.gms:play-services-location",
                Versions.Google.Play.LOCATION
            )
        }

        object Maps {
            object V3 {
                val MAPS =
                    dependency("com.google.android.libraries.maps:maps", Versions.Google.Maps.MAPS)
                val MAPS_KTX =
                    dependency("com.google.maps.android:maps-v3-ktx", Versions.Google.Maps.KTX)

                val MAPS_UTIL = dependency(
                    "com.google.maps.android:android-maps-utils-v3",
                    Versions.Google.Maps.UTIL
                )
                val MAPS_UTIL_KTX = dependency(
                    "com.google.maps.android:maps-utils-v3-ktx",
                    Versions.Google.Maps.KTX
                )
            }
        }
    }

    object Kotlin {
        private fun kotlin(name: String) = dependency(name, Versions.KOTLIN)

        val STDLIB = kotlin("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        val REFLECT = kotlin("org.jetbrains.kotlin:kotlin-reflect")

        object Coroutines {
            private fun coroutines(name: String) = dependency(name, Versions.COROUTINES)

            val CORE = coroutines("org.jetbrains.kotlinx:kotlinx-coroutines-core")
            val ANDROID = coroutines("org.jetbrains.kotlinx:kotlinx-coroutines-android")
            val PLAY = coroutines("org.jetbrains.kotlinx:kotlinx-coroutines-play-services")
            val TEST = coroutines("org.jetbrains.kotlinx:kotlinx-coroutines-test")
        }
    }

    object Moshi {
        private fun moshi(name: String) = dependency(name, Versions.Square.MOSHI)

        val MOSHI = moshi("com.squareup.moshi:moshi")
        val KAPT_CODEGEN = moshi("com.squareup.moshi:moshi-kotlin-codegen")
    }

    object Retrofit {
        private fun retrofit(name: String) = dependency(name, Versions.Square.RETROFIT)

        val RETROFIT = retrofit("com.squareup.retrofit2:retrofit")

        object Converter {
            val MOSHI = retrofit("com.squareup.retrofit2:converter-moshi")
            val GSON = retrofit("com.squareup.retrofit2:converter-gson")
            val TEXT = retrofit("com.squareup.retrofit2:converter-scalars")
        }

        val MOCK = retrofit("com.squareup.retrofit2:retrofit-mock")
    }

    object Room {
        private fun room(name: String) = dependency(name, Versions.Jetpack.ROOM)

        val AP = room("androidx.room:room-compiler")
        val RUNTIME = room("androidx.room:room-runtime")
        val KTX = room("androidx.room:room-ktx")
        val TEST = room("androidx.room:room-testing")
    }

    object Test {
        object Jetpack {
            val CORE = dependency("androidx.test:core", Versions.Jetpack.Test.CORE)
            val RULES = dependency("androidx.test:rules", Versions.Jetpack.Test.CORE)
            val RUNNER = dependency("androidx.test:runner", Versions.Jetpack.Test.CORE)
            val ARCH_CORE = dependency(
                "androidx.arch.core:core-testing",
                Versions.Jetpack.Test.ARCH_CORE
            )

            object Espresso {
                private fun espresso(name: String) =
                    dependency(name, Versions.Jetpack.Test.ESPRESSO)

                val CORE = espresso("androidx.test.espresso:espresso-core")
                val CONTRIB = espresso("androidx.test.espresso:espresso-contrib")
                val INTENTS = espresso("androidx.test.espresso:espresso-intents")
                val ACCESSIBILITY = espresso("androidx.test.espresso:espresso-accessibility")
            }
        }

        val MOCKITO = dependency("org.mockito:mockito-core", Versions.MOCKITO)
        val MOCKK = dependency("io.mockk:mockk", Versions.MOCKK)
        val JUNIT = dependency("junit:junit", Versions.JUNIT)
        val OKHTTP_MOCK_SERVER =
            dependency("com.squareup.okhttp3:mockwebserver", Versions.Square.OKHTTP)
    }

    object Debug {
        val LEAK_CANARY = dependency(
            "com.squareup.leakcanary:leakcanary-android",
            Versions.Square.LEAK_CANARY
        )
    }

    private fun dependency(name: String, version: String): String {
        require(name.count { it == ':' } == 1) {
            "Dependency name must be in group:artifact format. [$name]"
        }
        return "$name:$version"
    }
}
