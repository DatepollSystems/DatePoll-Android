package com.bke.datepoll

import android.content.Context
import android.content.SharedPreferences

class Prefs(context: Context) {
    private val FILE = "settings"
    private val prefs: SharedPreferences = context.getSharedPreferences(FILE, 0)

    var INTRO: Boolean
        get() = prefs.getBoolean("introShown", false)
        set(value) = prefs.edit().putBoolean("introShown", value).apply()

    var SESSION: String?
        get() = prefs.getString("session", null)
        set(value) = prefs.edit().putString("session", value).apply()

    var SERVER_ADDRESS: String?
        get() = prefs.getString("server", "https://duckduckgo.com")
        set(value) = prefs.edit().putString("server", value).apply()

    var SERVER_PORT : Int
        get() = prefs.getInt("port", 9330)
        set(value) = prefs.edit().putInt("port", value).apply()

    var JWT: String?
        get() = prefs.getString("jwt", null)
        set(value) = prefs.edit().putString("jwt", value).apply()

    var JWT_RENEWAL_TIME: Long
        get() = prefs.getLong("jwtRenewalTime", 0)
        set(value) = prefs.edit().putLong("jwtRenewalTime", value).apply()

    var IS_LOGGED_IN: Boolean
        get() = prefs.getBoolean("loggedin", false)
        set(value) = prefs.edit().putBoolean("loggedin", value).apply()

    var THEME: String?
        get() = prefs.getString("theme", "Default")
        set(value) = prefs.edit().putString("theme", value).apply()
}