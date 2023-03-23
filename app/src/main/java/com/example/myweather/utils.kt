package com.example.myweather


import java.text.SimpleDateFormat
import java.util.*

fun getDate(timestamp: Int) :String {
    //val calendar = Calendar.getInstance(Locale.ENGLISH)
    //calendar.timeInMillis = timestamp * 1000L
    val date = Date (timestamp * 1000L)
    val formatter =  SimpleDateFormat("EEE, MMM d", Locale.ENGLISH)
    return formatter.format(date).toString()
}

