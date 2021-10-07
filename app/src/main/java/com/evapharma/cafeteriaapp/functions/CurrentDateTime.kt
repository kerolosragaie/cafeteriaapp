package com.evapharma.cafeteriaapp.functions

import java.text.SimpleDateFormat
import java.util.*

fun getCurrentDateAndTime():String{
    val sdf = SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z")
    return sdf.format(Date())
}