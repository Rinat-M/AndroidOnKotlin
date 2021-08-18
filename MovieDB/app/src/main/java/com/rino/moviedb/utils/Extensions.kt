package com.rino.moviedb.utils

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*
import javax.net.ssl.HttpsURLConnection

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

fun View.showSnackBar(
    msg: String,
    duration: Int = Snackbar.LENGTH_SHORT,
    @StringRes actionStringId: Int? = null,
    action: View.OnClickListener? = null
) {
    val snackBar = Snackbar.make(this, msg, duration)

    if (actionStringId != null && action != null) {
        snackBar.setAction(actionStringId, action)
    }

    snackBar.show()
}

fun BufferedReader.getLines(): String {
    val rawData = StringBuilder(1024)
    var line: String?

    while (this.readLine().also { line = it } != null) {
        rawData.append(line).append("\n")
    }

    this.close()
    return rawData.toString()
}

fun HttpsURLConnection.getLines(): String {
    this.requestMethod = "GET"
    this.readTimeout = 10000

    val bufferedReader = BufferedReader(InputStreamReader(this.inputStream))
    return bufferedReader.getLines()
}
