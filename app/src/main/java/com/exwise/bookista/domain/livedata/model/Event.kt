package com.exwise.bookista.domain.livedata.model

open class Event {
    var consumed = false
        private set

    fun consume() {
        consumed = true
    }
}