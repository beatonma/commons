import org.gradle.api.JavaVersion

private fun dependency(group: String, artifact: String, version: String) = "$group:$artifact:$version"
private fun androidx(artifact: String, version: String, group: String = artifact) = dependency("androidx.$group", artifact, version)
private fun bma(artifact: String, version: String = Versions.BMA) = dependency("org.beatonma.lib", artifact, version)
private fun retrofit(artifact: String, version: String = Versions.RETROFIT) = dependency("com.squareup.retrofit2", artifact, version)
private fun room(artifact: String, version: String = Versions.ROOM) = androidx(group = "room", artifact = artifact, version = version)

object Versions {
    const val BMA = "0.9.29"
    const val COROUTINES = "1.0.1"
    val JAVA = JavaVersion.VERSION_1_8
    const val KOTLIN = "1.3.31"
    const val RETROFIT = "2.1.0"
    const val ROOM = "2.1.0-beta01"
}

object Dependencies {

    val GLIDE = dependency("com.github.bumptech.glide", "glide", "3.7.0")
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

    object Kotlin {
        val STDLIB = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.KOTLIN}"
        val REFLECT = "org.jetbrains.kotlin:kotlin-reflect:${Versions.KOTLIN}"
        val COROUTINES_CORE = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.COROUTINES}"
        val COROUTINES_ANDROID = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.COROUTINES}"
    }

    object Retrofit {
        val RETROFIT = retrofit("retrofit")
        val RETROFIT_CONVERTER_GSON = retrofit("converter-gson")
    }

    object Room {
        val KAPT = room("room-compiler")
        val RUNTIME = room("room-runtime")
        val KTX = room("room-ktx")
    }

    object Test {
        val ANDROIDX_TEST_CORE = androidx(group = "test", artifact = "core", version = "1.1.0")
        val ANDROIDX_TEST_RUNNER = androidx(group = "test", artifact = "runner", version = "1.1.1-alpha01")
        val ANDROIDX_TEST_ESPRESSO = androidx(group = "test.espresso", artifact = "espresso-core", version = "3.1.1")

        val BMA_TESTING = bma("testing")
        val MOCKITO = dependency("org.mockito", "mockito-all", "1.10.19")
        val JUNIT = dependency("junit", "junit", "4.12")
    }
}
