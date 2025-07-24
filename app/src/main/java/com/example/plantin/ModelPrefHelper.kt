package com.example.plantin

import android.content.Context
import android.content.SharedPreferences

object ModelPrefHelper {
    private const val PREF_NAME = "model_prefs"
    private const val KEY_MODEL_READY = "model_ready"

    fun isModelReady(context: Context): Boolean {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_MODEL_READY, false)
    }

    fun setModelReady(context: Context, ready: Boolean) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_MODEL_READY, ready).apply()
    }
}
