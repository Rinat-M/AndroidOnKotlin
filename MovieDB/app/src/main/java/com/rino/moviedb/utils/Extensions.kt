package com.rino.moviedb.utils

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

fun String.toDate(format: String, locale: Locale = Locale.getDefault()) =
    SimpleDateFormat(format, locale).parse(this)!!

fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}

fun View.showSnackBar(
    @StringRes stringId: Int,
    duration: Int = Snackbar.LENGTH_SHORT,
    @StringRes actionStringId: Int? = null,
    action: View.OnClickListener? = null
) {
    val snackBar = Snackbar.make(this, stringId, duration)

    if (actionStringId != null && action != null) {
        snackBar.setAction(actionStringId, action)
    }

    snackBar.show()
}

