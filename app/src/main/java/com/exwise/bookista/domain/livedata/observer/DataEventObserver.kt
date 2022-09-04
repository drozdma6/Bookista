package com.exwise.bookista.domain.livedata.observer

import androidx.lifecycle.Observer
import com.exwise.bookista.domain.livedata.model.DataEvent

class DataEventObserver<T>(val onChangedCallback: (data: T?) -> Unit) : Observer<DataEvent<T>> {
    override fun onChanged(event: DataEvent<T>?) {
        if (event != null && !event.consumed) {
            event.consume()
            onChangedCallback(event.data)
        }
    }
}