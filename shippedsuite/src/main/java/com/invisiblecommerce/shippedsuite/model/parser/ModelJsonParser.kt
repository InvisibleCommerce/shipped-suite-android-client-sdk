package com.invisiblecommerce.shippedsuite.model.parser

import android.os.Build
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

interface ModelJsonParser<Model> {
    fun parse(json: JSONObject): Model

    val dateFormat: SimpleDateFormat
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSX", Locale.getDefault())
        } else {
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSS", Locale.getDefault())
        }
}
