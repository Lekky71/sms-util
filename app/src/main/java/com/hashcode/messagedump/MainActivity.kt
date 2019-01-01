package com.hashcode.messagedump

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.hashcode.messagedump.utils.PdfCreator
import com.hashcode.messagedump.utils.SmsFilter
import com.hashcode.messagedump.utils.SmsLogger
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.apache.commons.io.FileUtils
import java.io.File

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        SmsLogger.init(con = applicationContext)
        dumpButton.setOnClickListener {
            if(isFormValid()){
                val number = numberEditText.text.toString().trim()
                val query = number.substring(7, 10)
                val allSms = SmsLogger.getMessages(query, null, null)

                val fileName = "messageDump"
//                writeToFile(fileName, allSms.toString())

                val sortedName = "sortedMessages"
//                writeToFile(sortedName, SmsFilter.filterArray(allSms!!).toString())

                PdfCreator.createPdf(SmsFilter.filterArray(allSms!!))
                showMessage("Successfully saved file as pdf")

            }
        }

    }

    private fun writeToFile(fileName: String, content:String){
        val file = File(fileDir(), fileName + "_" + System.currentTimeMillis() + ".json")
        FileUtils.writeStringToFile(file, content)
        val intent = Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE")
        intent.data = Uri.fromFile(file)
        sendBroadcast(intent)
        showMessage("Successfully saved file to ${file.absolutePath!!}")
    }

    fun fileDir(): File {
        val dir = Environment.getExternalStoragePublicDirectory("MessageDump/exports")
        if (!dir.exists()) {
            val folderCreated = dir.mkdirs()
        }
        return dir
    }


    private fun isFormValid(): Boolean{
        if(numberEditText.text.toString().trim().length != 11){
            showMessage("Enter valid number")
            return false
        }
        return true
    }

    private fun showMessage(message: String){
        Snackbar.make(numberEditText, message, Snackbar.LENGTH_LONG).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
