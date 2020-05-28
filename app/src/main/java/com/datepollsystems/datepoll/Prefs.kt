package com.datepollsystems.datepoll

import android.content.Context
import android.content.SharedPreferences

class Prefs(context: Context) {
    private val FILE = "settings"
    private val _prefs: SharedPreferences = context.getSharedPreferences(FILE, 0)

    var intro: Boolean
        get() = _prefs.getBoolean("introShown", false)
        set(value) = _prefs.edit().putBoolean("introShown", value).apply()

    var session: String?
        get() = _prefs.getString("session", null)
        set(value) = _prefs.edit().putString("session", value).apply()

    var serverAddress: String?
        get() = _prefs.getString("server", "https://datepoll.org")
        set(value) = _prefs.edit().putString("server", value).apply()

    var serverPort : Int
        get() = _prefs.getInt("port", 443)
        set(value) = _prefs.edit().putInt("port", value).apply()

    var jwt: String?
        get() = _prefs.getString("jwt", null)
        set(value) = _prefs.edit().putString("jwt", value).apply()

    var jwtRenewalTime: Long
        get() = _prefs.getLong("jwtRenewalTime", 0)
        set(value) = _prefs.edit().putLong("jwtRenewalTime", value).apply()

    var isLoggedIn: Boolean
        get() = _prefs.getBoolean("loggedin", false)
        set(value) = _prefs.edit().putBoolean("loggedin", value).apply()

    var theme: String?
        get() = _prefs.getString("theme", "Default")
        set(value) = _prefs.edit().putString("theme", value).apply()

}
