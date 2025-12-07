package com.example.braincircle.model.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.abs

object DateUtils {
    fun formatMeetingDate(date: Date?): String {
        if (date == null) return ""

        val formatter = SimpleDateFormat("EEE, dd MMM - h:mm a", Locale.getDefault())
        return formatter.format(date)
    }

    fun getDateIntervalInDays(startDate: Date, endDate: Date): Long {
        val startCalendar = Calendar.getInstance().apply { time = startDate }
        val endCalendar = Calendar.getInstance().apply { time = endDate }

        val diffInMillis = abs(endCalendar.timeInMillis - startCalendar.timeInMillis)
        return TimeUnit.MILLISECONDS.toDays(diffInMillis)
    }
}