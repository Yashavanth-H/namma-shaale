package com.nammashaale.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("namma_shaale_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_SCHOOL_NAME = "school_name"
        private const val KEY_PRINCIPAL_NAME = "principal_name"
        private const val KEY_SCHOOL_ADDRESS = "school_address"
        private const val KEY_PIN = "user_pin"
    }

    fun saveSession(schoolName: String, principalName: String, address: String, pin: String) {
        prefs.edit()
            .putBoolean(KEY_IS_LOGGED_IN, true)
            .putString(KEY_SCHOOL_NAME, schoolName)
            .putString(KEY_PRINCIPAL_NAME, principalName)
            .putString(KEY_SCHOOL_ADDRESS, address)
            .putString(KEY_PIN, pin)
            .apply()
    }

    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_IS_LOGGED_IN, false)

    fun getSchoolName(): String = prefs.getString(KEY_SCHOOL_NAME, "My School") ?: "My School"
    fun getPrincipalName(): String = prefs.getString(KEY_PRINCIPAL_NAME, "") ?: ""
    fun getSchoolAddress(): String = prefs.getString(KEY_SCHOOL_ADDRESS, "") ?: ""
    fun getPin(): String = prefs.getString(KEY_PIN, "") ?: ""

    fun logout() {
        prefs.edit().putBoolean(KEY_IS_LOGGED_IN, false).apply()
    }
}
