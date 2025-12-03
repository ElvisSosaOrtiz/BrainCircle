package com.example.braincircle.model.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {
    fun formatMeetingDate(date: Date?): String {
        if (date == null) return ""

        val formatter = SimpleDateFormat("EEE, dd MMM - h:mm a", Locale.getDefault())
        return formatter.format(date)
    }
}