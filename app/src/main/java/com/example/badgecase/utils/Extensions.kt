package com.example.badgecase.utils

import com.example.badgecase.R
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by AralBenli on 8.12.2023.
 */

private val badgeIconMapping = mapOf(
    3 to R.drawable.m3,
    4 to R.drawable.m4,
    5 to R.drawable.m5,
    6 to R.drawable.m6,
    7 to R.drawable.m7,
    8 to R.drawable.m8,
    9 to R.drawable.m9,
    10 to R.drawable.m10,
    11 to R.drawable.m11,
)

fun getBadgeIconMapping(): Map<Int, Int> {
    return badgeIconMapping
}

fun String.parseDate(format: String): Pair<Date?, String> {
    return try {
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())
        val date = dateFormat.parse(this)

        val timeSuffix: String = when ((Calendar.getInstance().timeInMillis - date.time) / (1000 * 60 * 60 * 24)) {
            0L -> date?.let { SimpleDateFormat("HH:mm", Locale.getDefault()).format(it) } + " Gönderdi"
            1L -> "Dün, " + date?.let { SimpleDateFormat("HH:mm", Locale.getDefault()).format(it) } + " Gönderdi"
            else -> date?.let { SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(it) } + " Gönderdi"
        }

        Pair(date, timeSuffix)
    } catch (e: ParseException) {
        e.printStackTrace()
        Pair(null, "")
    }
}

fun Double.formatNumberWithComma(): String {
    val decimalFormat = DecimalFormat("#,##0.0", DecimalFormatSymbols(Locale.getDefault()))
    return decimalFormat.format(this).replace('.', ',')
}


fun Int.calculateTotalPages(): Int {
    return kotlin.math.ceil(this.toDouble() / 4).toInt()
}
