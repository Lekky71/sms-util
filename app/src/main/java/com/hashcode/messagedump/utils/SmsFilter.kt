package com.hashcode.messagedump.utils

import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

/**
 * Helper class to filter out messages needed from the bulk of messages to a particular number extracted.
 */

class SmsFilter {
    /**
     * Note: putting function inside a companion object makes it static, info, in case you are new to Kotlin.
     */

    companion object {
        private val TAG = SmsFilter::class.java.simpleName

        /**
         * @param jsonArray is the array of all messages sent and received between the user and a number.
         * @param minCharLength is the minimum character length that a message must have before it can be operated on.
         * Note: only SENT messages are operated on here. (What I needed for myself)
         * @return result which a JSON Object that uses the date as key and a concatenation of all the messages for that day as value.
         * i.e
         * {
         * "2018-8-23": "Hi",
        "2018-10-3": "Hello"
        "2018-10-4": "Hey, mate!"
        }
         */

        fun filterArray(jsonArray: JSONArray, minCharLength: Int): JSONObject {
            val result = JSONObject()
            Log.i(TAG, jsonArray.length().toString())

            for (i in jsonArray.length() - 1 downTo 0) {
                val jsonObject = jsonArray.getJSONObject(i)
                if (jsonObject.getString("type") == "SENT" && (jsonObject.getString("body").length > minCharLength)) {
                    val calendar = Calendar.getInstance()

                    calendar.timeInMillis = java.lang.Long.parseLong(jsonObject.getString("date"))

                    val key =
                        "${calendar.get(Calendar.YEAR)}-${calendar.get(Calendar.MONTH) + 1}-${calendar.get(Calendar.DAY_OF_MONTH)}"
                    val messageBody = jsonObject.getString("body")
                    if (result.has(key)) {
                        result.put(key, result.getString(key) + "\n\n" + messageBody)
                    } else {
                        result.put(key, messageBody)
                    }
                }
            }
            return result
        }
    }
}