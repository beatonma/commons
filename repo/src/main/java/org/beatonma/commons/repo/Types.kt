package org.beatonma.commons.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.flow.Flow
import org.beatonma.commons.repo.result.IoResult

// Wrappers to reduce deep nesting of type definitions <<<>>>
typealias IoResultList<T> = org.beatonma.commons.repo.result.IoResult<List<T>>
typealias LiveDataIoResult<T> = LiveData<IoResult<T>>
typealias LiveDataIoResultList<T> = LiveData<IoResultList<T>>
typealias IoResultObserver<T> = Observer<IoResult<T>>

typealias FlowIoResult<T> = Flow<IoResult<T>>
typealias FlowIoResultList<T> = Flow<IoResultList<T>>
