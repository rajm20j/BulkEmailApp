package com.example.bulkemailapp.utils

import android.os.Build
import android.text.Html
import android.text.Spanned
import android.text.TextUtils
import android.util.Patterns

fun String.isEmailId() =
    contains('@')

fun CharSequence.isValidEmail() =
    !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.parseHtml(): Spanned {
    return if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
        Html.fromHtml(this)
    } else {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    }
}
