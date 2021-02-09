package org.beatonma.commons.kotlin.extensions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T> withMainContext(block: suspend CoroutineScope.() -> T) =
    withContext(Dispatchers.Main, block = block)
