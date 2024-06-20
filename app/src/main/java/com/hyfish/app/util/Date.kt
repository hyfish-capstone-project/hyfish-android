package com.hyfish.app.util

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

const val API_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'"
const val READABLE_DATE_FORMAT = "dd MMM yyyy HH:mm:ss"

fun String.toReadableDate(): String {
    try {
        val parser = SimpleDateFormat(API_DATE_FORMAT, Locale.getDefault())
        parser.timeZone = TimeZone.getTimeZone("UTC")

        val date = parser.parse(this)!!

        val formatter = SimpleDateFormat(READABLE_DATE_FORMAT, Locale.getDefault())
        formatter.timeZone = TimeZone.getDefault()

        return formatter.format(date)
    } catch (e: Exception) {
        return this
    }
}
