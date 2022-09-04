package com.exwise.bookista.domain.livedata.model

open class DataEvent<T>(val data: T) : Event()