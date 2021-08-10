package com.rino.moviedb.utils

import java.text.SimpleDateFormat
import java.util.*

fun String.toDate(format: String, locale: Locale = Locale.getDefault()) =
    SimpleDateFormat(format, locale).parse(this)!!

fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}