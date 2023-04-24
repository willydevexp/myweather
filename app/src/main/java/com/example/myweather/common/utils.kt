package com.example.myweather.common


import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun getDate(timestamp: Int) :String {
    //val calendar = Calendar.getInstance(Locale.ENGLISH)
    //calendar.timeInMillis = timestamp * 1000L
    val date = Date (timestamp * 1000L)
    val formatter =  SimpleDateFormat("MMM d, EEE", Locale.ENGLISH)
    return formatter.format(date).toString()
}

