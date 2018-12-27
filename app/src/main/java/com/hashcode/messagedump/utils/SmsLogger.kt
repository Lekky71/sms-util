package com.hashcode.messagedump.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Telephony
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import com.hashcode.messagedump.PermissionActivity
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class SmsLogger {

    companion object {
        internal fun init(con: Context) {
            context = con
            askForPermissions()
        }

        @SuppressLint("StaticFieldLeak")
        private lateinit var context: Context
        private var ACTION_REQUEST_PERMISSION = "ACTION_REQUEST_PERMISSION"


        private fun askForPermissions() {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

                val permissionCheck1 = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_SMS
                ) == PackageManager.PERMISSION_GRANTED
                val permissionCheck2 = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.RECEIVE_SMS
                ) == PackageManager.PERMISSION_GRANTED
                val permissionCheck3 = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED

                if (!(permissionCheck1 && permissionCheck2 && permissionCheck3)){
                    val intent = Intent(context, PermissionActivity::class.java)
                    intent.action = ACTION_REQUEST_PERMISSION
                    context.startActivity(intent)
                } else {
                }
            } else {
            }

        }

        fun getMessages(address: String?, lim: String?, time: String?): JSONArray? {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_SMS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return null
            }

            var selection: String?
            var selectionArgs: Array<String>?
            if (address == null) {
                selection = null
                selectionArgs = null
            } else {
                selection = "address LIKE ?"
                selectionArgs = arrayOf("%$address%")
            }
            if (time != null) {
                if (selectionArgs != null) {
                    selection += " AND date >=?"
                    selectionArgs = arrayOf("%$address%", time)
                } else {
                    selection = "date >=?"
                    selectionArgs = arrayOf(time)
                }
            }

            val jsonArray = JSONArray()
            val cursor = context.contentResolver.query(
                Uri.parse("content://sms"), null,
                selection, selectionArgs, null
            )

            val colCount: Int
            if (cursor != null) {

                val limit: Int = try {
                    if (lim == null) cursor.count else Integer.valueOf(lim)
                } catch (exp: Exception) {
                    cursor.count
                }

                colCount = cursor.columnCount
                Log.i("Column count", "Column count $colCount")
                try {
                    var limitCounter = 0
                    while (cursor.moveToNext() && limitCounter < limit) {
                        limitCounter++
                        val jsonObject = JSONObject()
                        for (i in 0 until colCount) {
                            var value = if (cursor.getString(i) != null) cursor.getString(i) else ""
                            if (cursor.getColumnName(i) != null) {
                                if (cursor.getColumnName(i) == Telephony.Sms.TYPE) {
                                    when (Integer.valueOf(value)) {
                                        Telephony.Sms.MESSAGE_TYPE_ALL -> value = "ALL"
                                        Telephony.Sms.MESSAGE_TYPE_DRAFT -> value = "DRAFT"
                                        Telephony.Sms.MESSAGE_TYPE_FAILED -> value = "FAILED"
                                        Telephony.Sms.MESSAGE_TYPE_INBOX -> value = "INBOX"
                                        Telephony.Sms.MESSAGE_TYPE_OUTBOX -> value = "OUTBOX"
                                        Telephony.Sms.MESSAGE_TYPE_QUEUED -> value = "QUEUED"
                                        Telephony.Sms.MESSAGE_TYPE_SENT -> value = "SENT"
                                        else -> {
                                        }
                                    }
                                } else if (cursor.getColumnName(i) == Telephony.Sms.STATUS) {
                                    when (Integer.valueOf(value)) {
                                        Telephony.Sms.STATUS_NONE -> value = "NONE"
                                        Telephony.Sms.STATUS_COMPLETE -> value = "COMPLETE"
                                        Telephony.Sms.STATUS_FAILED -> value = "FAILED"
                                        Telephony.Sms.STATUS_PENDING -> value = "PENDING"
                                        else -> {
                                        }
                                    }
                                }
                                if (cursor.getString(i) != null) {
                                    jsonObject.put(cursor.getColumnName(i), value)
                                } else {
                                    jsonObject.put(cursor.getColumnName(i), "")
                                }
                            }
                        }
                        jsonArray.put(jsonObject)
                    }
                } catch (jse: JSONException) {

                }

                cursor.close()
            }
            return jsonArray
        }

    }
}