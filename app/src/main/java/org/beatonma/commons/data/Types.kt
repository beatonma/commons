package org.beatonma.commons.data

import androidx.lifecycle.LiveData
import retrofit2.Response

typealias ParliamentID = Int

// Wrappers to reduce deep nesting of type definitions <<<>>>
typealias IoResultList<T> = IoResult<List<T>>
typealias ListResponse<T> = Response<List<T>>
typealias LiveDataIoResult<T> = LiveData<IoResult<T>>
typealias LiveDataIoResultList<T> = LiveData<IoResultList<T>>
typealias LiveDataList<T> = LiveData<List<T>>
