package org.beatonma.commons.buildsrc.kts.extensions

import com.android.build.gradle.internal.dsl.DefaultConfig
import java.util.*

fun DefaultConfig.injectStrings(vararg mapping: Pair<String, String>, asBuildConfig: Boolean, asResValue: Boolean) {
    mapping.forEach { (key, value) ->
        if (asBuildConfig) {
            buildConfigString(key, value)
        }
        if (asResValue) {
            resString(key, value)
        }
    }
}

fun DefaultConfig.injectInts(vararg mapping: Pair<String, Int>, asBuildConfig: Boolean, asResValue: Boolean) {
    mapping.forEach { (key, value) ->
        if (asBuildConfig) {
            buildConfigInt(key, value)
        }
        if (asResValue) {
            resInt(key, value)
        }
    }
}

// BuildConfig values
fun DefaultConfig.buildConfigString(key: String, value: String) =
    buildConfigField("String", key.toUpperCase(Locale.getDefault()), "\"$value\"")

fun DefaultConfig.buildConfigInt(key: String, value: Int) =
    buildConfigField("int", key.toUpperCase(Locale.getDefault()), "$value")

// Resource values
fun DefaultConfig.resString(key: String, value: String) =
    resValue("string", key.toLowerCase(Locale.getDefault()), value)

fun DefaultConfig.resColor(key: String, value: String) =
    resValue("color", key.toLowerCase(Locale.getDefault()), value)

fun DefaultConfig.resColor(key: String, value: Int) = resColor(key, "$value")

fun DefaultConfig.resInt(key: String, value: String) =
    resValue("integer", key.toLowerCase(Locale.getDefault()), value)

fun DefaultConfig.resInt(key: String, value: Int) = resInt(key, "$value")
