package com.har.pianoclassic

import android.content.Context
import android.content.SharedPreferences

open class SettingsPrefSaver(context: Context?) {

    private var sharedPreferences: SharedPreferences

    init {
        sharedPreferences = context!!.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveHealth(health: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(KEY_HEALTH, health)
        editor.apply()
    }

    val keyHealth: Int get() = sharedPreferences.getInt(KEY_HEALTH, 3)

    companion object {
        protected const val SHARED_PREF_NAME = "com.har.pianoclassic.sharedprefs"
        protected const val KEY_HEALTH = "HEALTH"
    }
}