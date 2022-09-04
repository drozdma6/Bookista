package com.exwise.bookista.domain.livedata.observer

import androidx.lifecycle.Observer
import com.exwise.bookista.domain.livedata.model.Event

class SpecificEventObserver<T : Event>(val onChangedCallback: (event: T) -> Unit) : Observer<T> {
    override fun onChanged(event: T?) {
        if (event != null && !event.consumed) {
            event.consume()
            onChangedCallback(event)
        }
    }
}