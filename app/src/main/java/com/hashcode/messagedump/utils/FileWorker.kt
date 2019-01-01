package com.hashcode.messagedump.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import com.hashcode.messagedump.R
import org.apache.commons.io.FileUtils
import org.json.JSONArray
import java.io.File

class FileWorker {

    companion object {
        @SuppressLint("StaticFieldLeak")
        var context: Context? = null
        fun init(context: Context){
            this.context = context;
        }

        /**
         * function to write
         * @param content
         * into file named
         * @param fileName
         */
        private fun writeToFile(fileName: String, content:String): String{
            val file = File(fileDir(), fileName + "_" + System.currentTimeMillis() + ".json")
            FileUtils.writeStringToFile(file, content)
            val intent = Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE")
            intent.data = Uri.fromFile(file)
            context!!.sendBroadcast(intent)
            return file.absolutePath!!
        }



        /**
         * function to create an app directory on the users external storage
         * @return a file object referencing the folder created.
         */
        fun fileDir(): File {
            val dir = Environment.getExternalStoragePublicDirectory("MessageDump/exports")
            if (!dir.exists()) {
                val folderCreated = dir.mkdirs()
            }
            return dir
        }

        /**
         * fetches all messages sent to
         * @param number
         *      * @return a JSONArray containing raw strings
         */
        fun getAllSentSms(number: String): JSONArray?{
            return SmsLogger.getMessages(number.substring(7, 10), null, null)
        }

        /**
         * Saves all the raw messages in one file
         */
        fun dumpAllSms(number: String): String{
            val fileName = "messageDump"
            return writeToFile(fileName, getAllSentSms(number)!!.toString())
        }

        /**
         * Saves all the raw messages in one file
         */
        fun dumpFilteredSms(number: String): String{
            val sortedName = "sortedMessages"
            return writeToFile(sortedName, SmsFilter.filterArray(getAllSentSms(number)!!, 85).toString())
        }

        /**
         * Generates a pdf file containing all sent messages with character length > 85
         * You can change the constraint as seen below.
         */
        fun generateSmsPdf(number: String): Boolean{
            return try {
                PdfCreator.createPdf(
                    SmsFilter.filterArray(getAllSentSms(number)!!, 85),
                    context!!.resources.getString(R.string.first_page_text),
                    context!!.resources.getString(R.string.last_page_text))
                true
            } catch (e: Exception){
                Toast.makeText(context, "An error occured", Toast.LENGTH_LONG).show()
                e.printStackTrace()
                false
            }
        }

    }
}