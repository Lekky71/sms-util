package com.hashcode.messagedump.permission

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.hashcode.messagedump.R

/**
 * Helper Activity to request runtime permissions from user
 */
class PermissionActivity : AppCompatActivity() {

    private lateinit var context: Context
    private val MY_PERMISSIONS_REQUEST = 7304
    private var ACTION_REQUEST_PERMISSION = "ACTION_REQUEST_PERMISSION"

    private val TAG = PermissionActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)
        context = applicationContext
        if (supportActionBar != null) {
            supportActionBar!!.title = ""
            supportActionBar!!.hide()
        }
        val intent = intent
        if (intent.action == ACTION_REQUEST_PERMISSION) {
            askForPermissions()
        }
    }

    private fun askForPermissions() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
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

                if (!(permissionCheck1 && permissionCheck2 && permissionCheck3)) {
                    ActivityCompat.requestPermissions(
                        this@PermissionActivity,
                        arrayOf(
                            Manifest.permission.READ_SMS,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.RECEIVE_SMS
                        ),
                        MY_PERMISSIONS_REQUEST
                    )
                    Log.i(TAG, "No permissions")

                } else {
                    Log.i(TAG, "Has permissions")
                    finish()
                }
            } else {
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST -> for (i in grantResults.indices) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    askForPermissions()
                    break
                }
                finish()
            }
            else -> {
            }
        }
    }

}
