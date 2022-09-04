package com.exwise.bookista.helper

import android.util.Patterns

class EmailValidator {
    companion object {
        fun validateEmail(text: String): Boolean {
            return text.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(text).matches()
        }
    }
}