package com.rino.moviedb.utils

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.provider.Settings
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.rino.moviedb.R
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.NumberFormat
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

fun Context.showToast(@StringRes stringId: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, stringId, duration).show()
}

fun Context.showToast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, duration).show()
}

fun AppCompatImageView.processFavorite(isFavorite: Boolean) {
    if (isFavorite) {
        setColorFilter(
            ContextCompat.getColor(this.context, R.color.amber_300),
            PorterDuff.Mode.SRC_IN
        )
    } else {
        clearColorFilter()
    }
}

fun Context.openAppSystemSettings() {
    val settingsIntent = Intent().apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        data = Uri.fromParts("package", packageName, null)
    }

    startActivity(settingsIntent)
}

fun Context.makeCall(phoneNumber: String) {
    val callIntent = Intent(Intent.ACTION_DIAL).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        data = Uri.parse("tel:$phoneNumber")
    }

    startActivity(callIntent)
}

fun Context.sendSms(phoneNumber: String, msg: String) {
    val sendToIntent = Intent(Intent.ACTION_SENDTO).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        type = "text/plain"
        putExtra("sms_body", msg);
        data = Uri.parse("smsto:$phoneNumber")
    }

    startActivity(sendToIntent)
}

fun Long.formatCurrency(): String {
    val locale = Locale("en", "US")
    return NumberFormat.getCurrencyInstance(locale).format(this)
}

fun Context.hideKeyboard(view: View) {
    val imm: InputMethodManager =
        this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Float.roundTo(n: Int): Float = "%.${n}f".format(Locale.ENGLISH, this).toFloat()

fun Double.roundTo(n: Int): Double = "%.${n}f".format(Locale.ENGLISH, this).toDouble()