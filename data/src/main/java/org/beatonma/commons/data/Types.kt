package org.beatonma.commons.data

import kotlinx.coroutines.flow.Flow

// Wrappers to reduce deep nesting of type definitions <<<>>>
typealias FlowList<T> = Flow<List<T>>
