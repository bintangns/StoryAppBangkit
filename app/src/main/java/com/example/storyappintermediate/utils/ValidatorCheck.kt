package com.example.storyappintermediate.utils

import android.text.Editable
import android.text.TextWatcher

fun isValidEmail(email: String): Boolean {
    val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)*(\\.[a-z]{2,})"
    return email.matches(emailPattern.toRegex())
}

fun isValidPassword(password: String): Boolean {
    return password.length >= 8
}

fun createTextWatcher(onTextChanged: (CharSequence?, Int, Int, Int) -> Unit): TextWatcher {
    return object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChanged(s, start, before, count)
        }

        override fun afterTextChanged(s: Editable?) {
        }
    }
}

