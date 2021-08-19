package org.beatonma.commons.app.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.beatonma.commons.repo.result.IoLoading
import org.beatonma.commons.repo.result.IoResult


abstract class IoLiveDataViewModel<T>: ViewModel() {
    private val _livedata: MutableLiveData<IoResult<T>> = MutableLiveData(IoLoading)
    val livedata: LiveData<IoResult<T>> = _livedata

    fun postValue(data: IoResult<T>) {
        _livedata.postValue(data)
    }
}
