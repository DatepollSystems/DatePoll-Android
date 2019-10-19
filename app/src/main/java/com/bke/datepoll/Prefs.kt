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

    var JWT: String?
        get() = prefs.getString("jwt", null)
        set(value) = prefs.edit().putString("jwt", value).apply()

}
