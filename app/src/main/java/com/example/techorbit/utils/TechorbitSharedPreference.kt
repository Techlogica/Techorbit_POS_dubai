package com.example.techorbit.utils

import android.content.Context
import android.content.SharedPreferences

class TechorbitSharedPreference(context: Context) {
    private var sharedPreferences: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null

    init {
        if (sharedPreferences == null) {
            sharedPreferences =
                context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
            editor = sharedPreferences!!.edit()
        }
    }

    fun save(key: String?, value: String) {
        editor?.putString(key, value)
        editor?.commit()
    }

    fun getValue(key: String?): String? {
        val text: String? = sharedPreferences?.getString(key, null)
        return text
    }

    fun removeAll() {
        editor?.clear()
        editor?.commit()

    }
}