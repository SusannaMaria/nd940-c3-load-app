package com.udacity.loadapp

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.udacity.NOTIFICATION_ID
import com.udacity.R
import com.udacity.databinding.ActivityDetailBinding
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {
    private var fileName = ""
    private var status = ""

    private lateinit var notificationManager: NotificationManager
    private lateinit var activityDetailBinding: ActivityDetailBinding

    companion object {
        const val EXTRA_DETAIL_STATUS = "status"
        const val EXTRA_DETAIL_FILENAME = "fileName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        fileName = intent.getStringExtra("fileName").toString()
        status = intent.getStringExtra("status").toString()
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(NOTIFICATION_ID)
        file_name.text = fileName
        status_text.text = status

        button.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

}
