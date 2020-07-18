package com.datepollsystems.datepoll.core

import android.content.Context
import android.content.SharedPreferences

class Prefs(context: Context) {
    private val FILE = "settings"
    val sharedPreferences: SharedPreferences = context.getSharedPreferences(FILE, 0)

    var intro: Boolean
        get() = sharedPreferences.getBoolean("introShown", false)
        set(value) = sharedPreferences.edit().putBoolean("introShown", value).apply()

    var session: String?
        get() = sharedPreferences.getString("session", null)
        set(value) = sharedPreferences.edit().putString("session", value).apply()

    var serverAddress: String?
        get() = sharedPreferences.getString("server", "https://datepoll.org")
        set(value) = sharedPreferences.edit().putString("server", value).apply()

    var serverPort : Int
        get() = sharedPreferences.getInt("port", 443)
        set(value) = sharedPreferences.edit().putInt("port", value).apply()

    var jwt: String?
        get() = sharedPreferences.getString("jwt", null)
        set(value) = sharedPreferences.edit().putString("jwt", value).apply()

    var jwtRenewalTime: Long
        get() = sharedPreferences.getLong("jwtRenewalTime", 0)
        set(value) = sharedPreferences.edit().putLong("jwtRenewalTime", value).apply()

    var isLoggedIn: Boolean
        get() = sharedPreferences.getBoolean("loggedin", false)
        set(value) = sharedPreferences.edit().putBoolean("loggedin", value).apply()

    var theme: String?
        get() = sharedPreferences.getString("theme", "Default")
        set(value) = sharedPreferences.edit().putString("theme", value).apply()

}
