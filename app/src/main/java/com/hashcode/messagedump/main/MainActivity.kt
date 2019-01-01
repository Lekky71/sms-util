package com.hashcode.messagedump.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.hashcode.messagedump.R
import com.hashcode.messagedump.utils.FileWorker
import com.hashcode.messagedump.utils.PdfCreator
import com.hashcode.messagedump.utils.SmsFilter
import com.hashcode.messagedump.utils.SmsLogger
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.apache.commons.io.FileUtils
import org.json.JSONArray
import java.io.File

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        SmsLogger.init(con = applicationContext)
        dumpButton.setOnClickListener {
            if(isFormValid(11)){
                val number = numberEditText.text.toString().trim()
                val worked:Boolean = FileWorker.generateSmsPdf(number)
                if(worked){
                    showMessage("Successfully generated the pdf, check ${FileWorker.fileDir().absolutePath}")
                }
            }
        }
    }


    /**
     * Number input text form validation
     * @param exactLength is the number of characters the number must have.
     */
    private fun isFormValid(exactLength: Int): Boolean{
        if(numberEditText.text.toString().trim().length != exactLength){
            showMessage("Enter a valid number")
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
