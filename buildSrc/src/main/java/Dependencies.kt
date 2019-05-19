import org.gradle.api.JavaVersion

private fun dependency(group: String, artifact: String, version: String) = "$group:$artifact:$version"
private fun androidx(artifact: String, version: String, group: String = artifact) =
    dependency("androidx.$group", artifact, version)

private fun bma(artifact: String, version: String = Versions.BMA) = dependency("org.beatonma.lib", artifact, version)
private fun dagger(artifact: String, version: String = Versions.DAGGER) =
    dependency("com.google.dagger", artifact, version)

private fun glide(artifact: String, version: String = Versions.GLIDE) =
    dependency("com.github.bumptech.glide", artifact, version)

private fun retrofit(artifact: String, version: String = Versions.RETROFIT) =
    dependency("com.squareup.retrofit2", artifact, version)

private fun room(artifact: String, version: String = Versions.ROOM) =
    androidx(group = "room", artifact = artifact, version = version)

object Versions {
    const val BMA = "0.9.29"
    const val COROUTINES = "1.0.1"
    const val DAGGER = "2.22.1"
    const val GLIDE = "3.7.0"
    val JAVA = JavaVersion.VERSION_1_8
    const val KOTLIN = "1.3.31"
    const val RETROFIT = "2.1.0"
    const val ROOM = "2.1.0-beta01"
}

object Dependencies {
    val GOOGLE_MATERIAL = dependency("com.google.android.material", "material", "1.1.0-alpha06")

    object AndroidX {
        val APPCOMPAT = androidx("appcompat", version = "1.1.0-alpha05")
        val CONSTRAINTLAYOUT = androidx("constraintlayout", version = "2.0.0-beta1")
        val CORE_KTX = androidx(group = "core", artifact = "core-ktx", version = "1.0.2")
        val LIFECYCLE_EXT = androidx(group = "lifecycle", artifact = "lifecycle-extensions", version = "2.0.0")
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

    object Dagger {
        val DAGGER = dagger("dagger")
        val RUNTIME = dagger("dagger-android")
        val SUPPORT = dagger("dagger-android-support")
        val COMPILER = dagger("dagger-compiler")
        val AP = dagger("dagger-android-processor")
        val SPI = dagger("dagger-spi")
    }

    object Glide {
        val COMPILER = glide("compiler")

        val ANNOTATIONS = glide("annotations")
        val CORE = glide("glide")
        val OKHTTP = glide("okhttp3-integration")
        val RECYCLERVIEW = glide("recyclerview-integration")
    }

    object Kotlin {
        val STDLIB = dependency("org.jetbrains.kotlin", "kotlin-stdlib-jdk8", Versions.KOTLIN)
        val REFLECT = dependency("org.jetbrains.kotlin", "kotlin-reflect", Versions.KOTLIN)

        object Coroutines {
            val CORE = dependency("org.jetbrains.kotlinx", "kotlinx-coroutines-core", Versions.COROUTINES)
            val ANDROID =
                dependency("org.jetbrains.kotlinx", "kotlinx-coroutines-android", Versions.COROUTINES)
        }
    }

    object Retrofit {
        val RETROFIT = retrofit("retrofit")

        object Converter {
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
            val CORE = androidx(group = "test", artifact = "core", version = "1.1.0")
            val RUNNER = androidx(group = "test", artifact = "runner", version = "1.1.1-alpha01")
            val ESPRESSO =
                androidx(group = "test.espresso", artifact = "espresso-core", version = "3.1.1")
        }

        val BMA_TESTING = bma("testing")
        val MOCKITO = dependency("org.mockito", "mockito-all", "1.10.19")
        val JUNIT = dependency("junit", "junit", "4.12")
    }
}
