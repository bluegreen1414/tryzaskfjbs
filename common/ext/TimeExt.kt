package com.kapirti.social_chat_food_video.common.ext

import android.text.format.DateUtils
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

fun timeCustomFormat(timestamps: Long): String {
    return SimpleDateFormat(DateFormats.DATE_TIME_FORMAT_CUSTOM_FORMAT.format, Locale.ENGLISH).format(timestamps * 1000)
}

fun getTimeFromTimestamp(timestamp: Long): String {
    return SimpleDateFormat(DateFormats.TIME_AM_PM_FORMAT.format, Locale.ENGLISH).format(timestamp * 1000)
}

fun getYearFromTimeStamp(timestamp: Long): String {
    return SimpleDateFormat("yyyy", Locale.ENGLISH).format(timestamp * 1000)
}

fun getBirthdayYear(birthday: String): String{
    val birthdayList: List<String> = birthday.split(" ").map{ it -> it.trim() }
    return birthdayList.last()
}

fun calculateBirthday(timestamp: Timestamp, birthday: String): String {
    val timestampLong = timestamp.seconds
    val currentYear =  getYearFromTimeStamp(timestampLong)
    val birthdayYear = getBirthdayYear(birthday)
    return try {
        (currentYear.toInt() - birthdayYear.toInt()).toString()
    } catch(e: NumberFormatException){
        "0"
    }
}


fun getRelativeTime(timestampMillis: Long): CharSequence {
    return DateUtils.getRelativeTimeSpanString(
        timestampMillis,
        System.currentTimeMillis(),
        DateUtils.SECOND_IN_MILLIS
    )
}


enum class DateFormats(val format: String) {
    DEFAULT_FORMAT("yyyy-MM-dd'T'HH:mm:ss"), //2021-05-20T11:28:24Z


    DEFAULT_FORMAT_WITHOUT_TIME("yyyy-MM-dd"), //2021-05-20
    DATE_TIME_YY_MM_FORMAT("yyyy-MM-dd'T'HH:mm"), //2021-05-20T11:28
    DATE_TIME_YY_MM_FULL_TIME_FORMAT("yyyy-MM-dd'T'HH:mm:ss"), //2021-05-20T11:28
    DATE_MM_DD_YY_FORMAT("MM-dd-yyyy"), //05-20-2021
    DATE_MM_DD_FORMAT("MMM dd, yyyy"), //May 20, 2021
    FULL_DATE_TIME_DD_MM_FORMAT("dd-MM-yyyy'T'HH:mm:ss"), //2021-05-20T11:28:24
    DATE_YY_MM_FORMAT("yyyy-MM-dd"), //2021-05-20
    DATE_MONTH_OF_YEAR_FORMAT("d MMMM, yyyy"), //20 May, 2021
    DAY_OF_WEEK_MONTH_FORMAT("EEE, d MMM"), //Thu, 20 May
    DATE_MONTH_FORMAT("d MMM"), // 20 MAY
    DATE_TIME_FORMAT_CUSTOM_FORMAT("dd.MM.yyyy' - ' HH:mm:ss"), //21.06.2021 - 10:10:18
    TIME_AM_PM_FORMAT("hh:mm a"), // 10:10:18 hh:mm aa
}
