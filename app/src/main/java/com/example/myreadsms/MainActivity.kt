package com.example.myreadsms

import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.myreadsms.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your
                // app.
            }else{

            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        when {
            ContextCompat.checkSelfPermission(
                applicationContext,
                android.Manifest.permission.READ_SMS
            ) == PackageManager.PERMISSION_GRANTED -> {
                getSMS()
            }
            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                requestPermissionLauncher.launch(
                    android.Manifest.permission.READ_SMS)
            }
        }
    }

    private fun getSMS(){
        val uri = Uri.parse("content://sms/inbox")
        val projection = arrayOf(SMS_SENDER, SMS_BODY)
        var cursor : Cursor = contentResolver.query(uri,projection,null,null,null)!!
        while (cursor.moveToNext()){
            for (i in 0 until cursor.columnCount){
                Log.i(LOG_TAG,"${i} - ${ cursor.getColumnName(i)} - ${cursor.getString(i)}")
            }
        }

    }


    companion object{
        private const val LOG_TAG = "MAIN ACTIVITY"
        private const val SMS_BODY = "body"
        private const val SMS_SENDER = "address"
    }
}