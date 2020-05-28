package com.example.bulkemailapp.utils

import android.os.Build
import android.text.Html
import android.text.Spanned

/**
 * Created by Divya Gupta on 12-Dec-19.
 **/

fun String.parseHtml(): Spanned {
    return if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
        Html.fromHtml(this)
    } else {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    }
}