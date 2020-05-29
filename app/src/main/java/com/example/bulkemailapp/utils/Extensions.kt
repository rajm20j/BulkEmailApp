package com.example.bulkemailapp.utils

import android.os.Build
import android.text.Html
import android.text.Spanned
import android.text.TextUtils

fun String.isEmailId() : Boolean
{
    return this.contains('@')
}

fun String.parseHtml(): Spanned {
    return if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
        Html.fromHtml(this)
    } else {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    }
}
