package org.beatonma.commons.buildsrc.kts.extensions

import com.android.build.gradle.internal.dsl.BuildType
import org.gradle.api.NamedDomainObjectContainer
import java.util.*

fun BuildType.buildConfigStrings(vararg mapping: Pair<String, String>) =
    mapping.forEach { (key, value) ->
        buildConfigString(key, value)
    }

fun BuildType.buildConfigInts(vararg mapping: Pair<String, Int>) =
    mapping.forEach { (key, value) ->
        buildConfigInt(key, value)
    }

fun BuildType.buildConfigString(key: String, value: String) =
    buildConfigField("String", key.toUpperCase(Locale.getDefault()), "\"$value\"")

fun BuildType.buildConfigInt(key: String, value: Int) =
    buildConfigField("int", key.toUpperCase(Locale.getDefault()), "$value")

fun BuildType.minify(
    enabled: Boolean = true,
    optimizeCode: Boolean = enabled,
    obfuscate: Boolean = enabled,
    removeUnusedCode: Boolean = enabled,
    removeUnusedResources: Boolean = enabled
) {
    postprocessing.apply {
        isOptimizeCode = optimizeCode
        isObfuscate = obfuscate
        isRemoveUnusedCode = removeUnusedCode
        isRemoveUnusedResources = removeUnusedResources
    }
}

fun NamedDomainObjectContainer<BuildType>.debug(block: BuildType.() -> Unit) {
    getByName("debug") {
        block()
    }
}

fun NamedDomainObjectContainer<BuildType>.release(block: BuildType.() -> Unit) {
    getByName("release") {
        block()
    }
}
