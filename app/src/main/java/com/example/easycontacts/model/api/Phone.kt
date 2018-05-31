package com.example.easycontacts.model.api

import com.squareup.moshi.Json

/**
 * Created by xkrej63 on 29.05.2018.
 */

class Phone(var type: String, @field:Json(name = "phone_number") var phone: String)
