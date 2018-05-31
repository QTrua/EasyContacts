package com.example.easycontacts.extension

import android.database.Cursor

/**
 * Created by xkrej63 on 31.05.2018.
 */

fun Cursor.getStringByColumnIndex(columnName: String) : String {
    return getString(getColumnIndex(columnName));
}