package com.evapharma.cafeteriaapp


import android.util.Patterns
import java.util.regex.Matcher
import java.util.regex.Pattern

fun isValidUrl(url: String): Boolean {
    val p: Pattern = Patterns.WEB_URL
    val m: Matcher = p.matcher(url.toLowerCase())
    return m.matches()
}

