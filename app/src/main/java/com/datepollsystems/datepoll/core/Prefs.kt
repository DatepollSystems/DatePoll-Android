package com.datepollsystems.datepoll.core

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class Prefs private constructor(context: Context) {

    private val oldSharedPreferences: SharedPreferences = context.getSharedPreferences("settings", 0)
    private val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
    private val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)

    private val sharedPreferences = EncryptedSharedPreferences
        .create(
            "settingsEncrypted",
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    companion object {

        @Volatile
        private var instance: Prefs? = null

        fun getInstance(context: Context): Prefs {
            val tmpInstance = instance

            if(tmpInstance != null)
                return tmpInstance

            return synchronized(this) {
                val created = Prefs(context = context)
                created.handleMigrationToEncryptedPrefs()
                instance = created
                created
            }

        }
    }

    fun handleMigrationToEncryptedPrefs(){
        if(!oldSession.isNullOrBlank()){
            intro = oldIntro
            session = oldSession
            serverAddress = oldServerAddress
            serverPort = oldServerPort
            jwt = oldJwt
            jwtRenewalTime = oldJwtRenewalTime
            isLoggedIn = oldIsLoggedIn
            theme = oldTheme

            oldSession = null
            oldJwt = null
            oldJwt = null
        }
    }

    var intro: Boolean
        get() = sharedPreferences.getBoolean("introShown", false)
        set(value) = sharedPreferences.edit().putBoolean("introShown", value).apply()

    var session: String?
        get() = sharedPreferences.getString("session", null)
        set(value) = sharedPreferences.edit().putString("session", value).apply()

    var serverAddress: String?
        get() = sharedPreferences.getString("server", "https://datepoll.org")
        set(value) = sharedPreferences.edit().putString("server", value).apply()

    var serverPort: Int
        get() = sharedPreferences.getInt("port", 443)
        set(value) = sharedPreferences.edit().putInt("port", value).apply()

    var jwt: String?
        get() = sharedPreferences.getString("jwt", "")
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


    // region old values before encryption
    private var oldIntro: Boolean
        get() = oldSharedPreferences.getBoolean("introShown", false)
        set(value) = oldSharedPreferences.edit().putBoolean("introShown", value).apply()

    private var oldSession: String?
        get() = oldSharedPreferences.getString("session", null)
        set(value) = oldSharedPreferences.edit().putString("session", value).apply()

    private var oldServerAddress: String?
        get() = oldSharedPreferences.getString("server", "https://datepoll.org")
        set(value) = oldSharedPreferences.edit().putString("server", value).apply()

    private var oldServerPort: Int
        get() = oldSharedPreferences.getInt("port", 443)
        set(value) = oldSharedPreferences.edit().putInt("port", value).apply()

    private var oldJwt: String?
        get() = oldSharedPreferences.getString("jwt", null)
        set(value) = oldSharedPreferences.edit().putString("jwt", value).apply()

    private var oldJwtRenewalTime: Long
        get() = oldSharedPreferences.getLong("jwtRenewalTime", 0)
        set(value) = oldSharedPreferences.edit().putLong("jwtRenewalTime", value).apply()

    private var oldIsLoggedIn: Boolean
        get() = oldSharedPreferences.getBoolean("loggedin", false)
        set(value) = oldSharedPreferences.edit().putBoolean("loggedin", value).apply()

    private var oldTheme: String?
        get() = oldSharedPreferences.getString("theme", "Default")
        set(value) = oldSharedPreferences.edit().putString("theme", value).apply()
}
