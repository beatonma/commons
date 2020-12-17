package org.beatonma.commons.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.flow.Flow
import org.beatonma.commons.repo.result.IoResult

// Wrappers to reduce deep nesting of type definitions <<<>>>
typealias ResultLiveData<T> = LiveData<IoResult<T>>
typealias ResultObserver<T> = Observer<IoResult<T>>

typealias ResultFlow<T> = Flow<IoResult<T>>
