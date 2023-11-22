package com.example.pelanggan_servis.Helper

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesHelper(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("akun", Context.MODE_PRIVATE)

    fun saveString(pairs: Map<String, String>) {
        val editor = sharedPreferences.edit()
        for ((key, value) in pairs) {
            editor.putString(key, value)
        }
        editor.commit()
    }

    fun getString(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun remove(keys: List<String>) {
        val editor = sharedPreferences.edit()
        for (key in keys) {
            editor.remove(key)
        }
        editor.apply()
    }
}
