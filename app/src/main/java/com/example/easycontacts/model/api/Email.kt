package com.example.easycontacts.model.api

import android.arch.persistence.room.Entity

/**
 * Created by xkrej63 on 29.05.2018.
 */

@Entity
class Email(val type: String, var email: String)
