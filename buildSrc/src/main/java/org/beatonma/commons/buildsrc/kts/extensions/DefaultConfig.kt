package org.beatonma.commons.buildsrc.kts.extensions

import com.android.build.gradle.internal.dsl.DefaultConfig
import java.util.*

fun DefaultConfig.injectStrings(vararg mapping: Pair<String, String>, asBuildConfig: Boolean, asResValue: Boolean) {
    mapping.forEach { (key, value) ->
        if (asBuildConfig) {
            buildConfigField("String", key.toUpperCase(Locale.getDefault()), "\"$value\"")
        }
        if (asResValue) {
            resValue("string", key.toLowerCase(Locale.getDefault()), value)
        }
    }
}

fun DefaultConfig.injectInts(vararg mapping: Pair<String, Int>, asBuildConfig: Boolean, asResValue: Boolean) {
    mapping.forEach { (key, value) ->
        if (asBuildConfig) {
            buildConfigField("int", key.toUpperCase(Locale.getDefault()), "$value")
        }
        if (asResValue) {
            resValue("integer", key.toLowerCase(Locale.getDefault()), "$value")
        }
    }
}
