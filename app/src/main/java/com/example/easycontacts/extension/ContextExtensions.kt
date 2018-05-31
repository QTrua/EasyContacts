package com.example.easycontacts.extension

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

/**
 * Created by xkrej63 on 31.05.2018.
 */
private const val SHARED_PREF_KEY = "credentials"

fun Context.getDefaultSharedPreferences() : SharedPreferences {
    return getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE)
}