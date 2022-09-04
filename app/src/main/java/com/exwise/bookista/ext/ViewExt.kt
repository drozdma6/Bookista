package com.exwise.bookista.ext

import android.view.View
import androidx.databinding.BindingAdapter

private const val timeInterval: Long = 350

/**
 * Only allows to press click once in 350ms
 */
class ThrottledClickListener(private val listener: View.OnClickListener) : View.OnClickListener {
    private var lastTime: Long = 0
    override fun onClick(view: View?) {
        view?.let {
            val currentTime = System.currentTimeMillis()
            if ((currentTime - lastTime) > timeInterval) {
                listener.onClick(view)
                lastTime = currentTime
            }
        }
    }
}

@BindingAdapter("onThrottledClick")
fun View.onThrottledClick(listener: View.OnClickListener) {
    setOnClickListener(ThrottledClickListener(listener))
}