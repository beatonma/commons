import org.gradle.api.JavaVersion


object Versions {
    const val BMA = "0.9.29"
    const val COMPOSE = "0.1.0-dev07"
    const val COROUTINES = "1.3.8-1.4.0-rc"
    const val DAGGER = "2.28.1"
    const val ESPRESSO = "3.3.0-beta02"
    const val GLIDE = "4.11.0"
    const val GROUPIE = "2.8.0"
    const val HILT = "2.28.1-alpha"
    const val AX_HILT = "1.0.0-alpha01"
    val JAVA = JavaVersion.VERSION_1_8
    const val KOTLIN = "1.4.0-rc"
    const val RETROFIT = "2.9.0"
    const val ROOM = "2.2.5"
    const val MOSHI = "1.9.2"
    const val AX_NAVIGATION = "2.3.0"
    const val AX_WORK = "2.4.0-rc01"
}

object Dependencies {
    object AndroidX {
        val APPCOMPAT = androidx("appcompat", version = "1.3.0-alpha01")
        val CONSTRAINTLAYOUT = androidx("constraintlayout", version = "2.0.0-beta8")
        val CORE_KTX = androidx(group = "core", artifact = "core-ktx", version = "1.5.0-alpha01")
        val LIFECYCLE_RUNTIME = androidx(group = "lifecycle", artifact = "lifecycle-runtime-ktx", version = "2.3.0-alpha05")
        val LIVEDATA_KTX = androidx(group = "lifecycle", artifact = "lifecycle-livedata-ktx", version = "2.3.0-alpha05")
        val VIEWMODEL_KTX = androidx(group = "lifecycle", artifact = "lifecycle-viewmodel-ktx", version = "2.3.0-alpha05")
        val ANNOTATIONS = androidx("annotation", version = "1.1.0")
        val RECYCLERVIEW = androidx(artifact = "recyclerview", version = "1.2.0-alpha03")
        val SWIPE_REFRESH_LAYOUT = androidx(artifact = "swiperefreshlayout", version = "1.1.0-rc01")

        val NAVIGATION_FRAGMENT = androidx(group = "navigation", artifact = "navigation-fragment-ktx", version = Versions.AX_NAVIGATION)
        val NAVIGATION_UI = androidx(group = "navigation", artifact = "navigation-ui-ktx", version = Versions.AX_NAVIGATION)

        val COMPOSE_TOOLING = androidx(group = "ui", artifact = "ui-tooling", version = Versions.COMPOSE)
        val COMPOSE_LAYOUT = androidx(group = "ui", artifact = "ui-layout", version = Versions.COMPOSE)
        val COMPOSE_MATERIAL = androidx(group = "ui", artifact = "ui-material", version = Versions.COMPOSE)

        val VIEWBINDING = androidx(group="databinding", artifact = "viewbinding", version = "4.1.0-alpha09")

        val WORK = androidx(group = "work", artifact = "work-runtime-ktx", version = Versions.AX_WORK)
    }

    object Bma {
        val ACTIVITY = bma("activity")
        val CORE = bma("core")
        val GRAPHIC_CORE = bma("graphic-core")
        val PAINTED_VIEW = bma("paintedview")
        val PREF = bma("pref")
        val RECYCLERVIEW = bma("recyclerview")
        val STYLE = bma("style")
        val UTIL = bma("util")
    }

    object Build {
        val VERSIONS = dependency("com.github.ben-manes", "gradle-versions-plugin", "0.21.0")
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
        val MATERIAL = dependency("com.google.android.material", "material", "1.2.0-alpha06")

        object Play {
            val AUTH = gms("play-services-auth", "18.0.0")
            val LOCATION = gms("play-services-location", "17.0.0")
            val MAPS = gms("play-services-maps", "17.0.0")
            val MAPS_UTIL = dependency("com.google.maps.android", artifact = "android-maps-utils-sdk-v3-compat", version = "0.1")
        }
    }

    object Groupie {
        val CORE = dependency("com.xwray", artifact = "groupie", version = Versions.GROUPIE)
        val KTX = dependency("com.xwray", artifact = "groupie-kotlin-android-extensions", version = Versions.GROUPIE)
        val VIEWBINDING = dependency("com.xwray", artifact = "groupie-viewbinding", version = Versions.GROUPIE)
    }

    object Kotlin {
        val STDLIB = kotlin("kotlin-stdlib-jdk8")
        val REFLECT = kotlin("kotlin-reflect")

        object Coroutines {
            val CORE = kotlinx("kotlinx-coroutines-core", Versions.COROUTINES)
            val ANDROID = kotlinx("kotlinx-coroutines-android", Versions.COROUTINES)
            val PLAY = kotlinx("kotlinx-coroutines-play-services", Versions.COROUTINES)
            val TEST = kotlinx("kotlinx-coroutines-test", Versions.COROUTINES)
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
            val CORE = androidx(group = "test", artifact = "core", version = "1.3.0-beta02")
            val RUNNER = androidx(group = "test", artifact = "runner", version = "1.3.0-beta02")
//            val ESPRESSO = androidx(group = "test.espresso", artifact = "espresso-core", version = "3.3.0-beta02")
            val LIVEDATA = androidx(group = "arch.core", artifact = "core-testing", version = "2.1.0")
            val RULES = androidx(group = "test", artifact = "rules", version = "1.2.0")
            object Espresso {
                val CORE = espresso("espresso-core")
                val CONTRIB = espresso("espresso-contrib")
                val INTENTS = espresso("espresso-intents")
                val ACCESSIBILITY = espresso("espresso-accessibility")
            }
        }



        val BMA_TESTING = bma("testing")
        val MOCKITO = dependency("org.mockito", "mockito-core", "3.3.3")
        val JUNIT = dependency("junit", "junit", "4.13")
        val RETROFIT_MOCK = retrofit("retrofit-mock")
        val ROOM = room("room-testing")
        val OKHTTP_MOCK_SERVER = dependency("com.squareup.okhttp3", "mockwebserver", "4.7.2")
    }
}



private fun dependency(group: String, artifact: String, version: String) = "$group:$artifact:$version"
private fun androidx(artifact: String, version: String, group: String = artifact) =
    dependency("androidx.$group", artifact, version)

private fun bma(artifact: String, version: String = Versions.BMA) = dependency("org.beatonma.lib", artifact, version)
private fun dagger(artifact: String, version: String = Versions.DAGGER) =
    dependency("com.google.dagger", artifact, version)

private fun hilt(artifact: String, version: String = Versions.HILT) = dagger(artifact = artifact, version = version)
private fun hiltAX(artifact: String, version: String = Versions.AX_HILT) = androidx(group = "hilt", artifact = artifact, version = version)

private fun espresso(artifact: String, version: String = Versions.ESPRESSO) =
    androidx(group = "test.espresso", artifact = artifact, version = version)

private fun glide(artifact: String, version: String = Versions.GLIDE) =
    dependency("com.github.bumptech.glide", artifact, version)

private fun gms(artifact: String, version: String) =
    dependency(group = "com.google.android.gms", artifact = artifact, version = version)

private fun kotlin(artifact: String, version: String = Versions.KOTLIN) =
    dependency(group = "org.jetbrains.kotlin", artifact = artifact, version = version)

private fun kotlinx(artifact: String, version: String = Versions.KOTLIN) =
    dependency(group = "org.jetbrains.kotlinx", artifact = artifact, version = version)

private fun retrofit(artifact: String, version: String = Versions.RETROFIT) =
    dependency("com.squareup.retrofit2", artifact, version)

private fun room(artifact: String, version: String = Versions.ROOM) =
    androidx(group = "room", artifact = artifact, version = version)

private fun moshi(artifact: String, version: String = Versions.MOSHI) =
    dependency("com.squareup.moshi", artifact, version)
