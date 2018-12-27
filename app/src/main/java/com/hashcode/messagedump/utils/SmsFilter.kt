package com.hashcode.messagedump.utils

import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

@Suppress("DEPRECATION")
class SmsFilter {


    companion object {
        private val TAG = SmsFilter::class.java.simpleName
        fun filterArray(jsonArray: JSONArray): JSONObject {
            val result = JSONObject()
            Log.i(TAG, jsonArray.length().toString())

            for (i in jsonArray.length()-1 downTo 0) {
                val jsonObject = jsonArray.getJSONObject(i)
                if (jsonObject.getString("type") == "SENT") {
                    val calendar = Calendar.getInstance()

                    calendar.timeInMillis = java.lang.Long.parseLong(jsonObject.getString("date"))

                    val key = "${calendar.get(Calendar.YEAR)}-${calendar.get(Calendar.MONTH) + 1}-${calendar.get(Calendar.DAY_OF_MONTH)}"
                    val messageBody = jsonObject.getString("body")
                    if (result.has(key)) {
                        result.put(key, result.getString(key) + messageBody)
                    } else {
                        result.put(key, messageBody)
                    }
                }
            }
            return result
        }
    }
}