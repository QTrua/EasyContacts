package com.example.easycontacts.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

import com.example.easycontacts.model.db.Contact

/**
 * Created by xkrej63 on 30.05.2018.
 */

@Database(entities = arrayOf(Contact::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao
}
