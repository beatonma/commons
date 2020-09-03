package org.beatonma.commons.buildsrc.kts.extensions

import Dependencies
import org.gradle.kotlin.dsl.DependencyHandlerScope

val DependencyHandlerScope.coroutines get() = arrayOf(
    Dependencies.Kotlin.Coroutines.ANDROID,
    Dependencies.Kotlin.Coroutines.CORE
)
