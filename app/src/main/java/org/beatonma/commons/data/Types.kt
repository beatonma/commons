package org.beatonma.commons.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

typealias ParliamentID = Int
typealias SnommocToken = String

// Wrappers to reduce deep nesting of type definitions <<<>>>
typealias IoResultList<T> = IoResult<List<T>>
typealias ListResponse<T> = Response<List<T>>
typealias LiveDataIoResult<T> = LiveData<IoResult<T>>
typealias LiveDataIoResultList<T> = LiveData<IoResultList<T>>
typealias LiveDataList<T> = LiveData<List<T>>
typealias IoResultObserver<T> = Observer<IoResult<T>>

typealias FlowIoResult<T> = Flow<IoResult<T>>
typealias FlowIoResultList<T> = Flow<IoResultList<T>>
typealias FlowList<T> = Flow<List<T>>
