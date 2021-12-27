package com.udacity

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import android.view.animation.Animation.INFINITE
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.view.marginLeft
import androidx.databinding.DataBindingUtil
import com.udacity.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import timber.log.Timber
import timber.log.Timber.DebugTree
import com.udacity.sendNotification

class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private var urlID: Int = -1
    private lateinit var notificationManager: NotificationManager
    private lateinit var activityMainBinding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(DebugTree())
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        imageView.setImageResource(R.mipmap.cloud_download);
        val radioGroup = findViewById<RadioGroup>(R.id.url_radio_group)

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            urlID = checkedId

            custom_button.isEnabled=true
            custom_button.isClickable = true
        }

        addRadioButtons()

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        activityMainBinding.lifecycleOwner = this


        custom_button.setOnClickListener {
            if (urlID<0){
                Toast.makeText(this@MainActivity, getString(R.string.user_select_file), Toast.LENGTH_SHORT).show()
            }else{
                custom_button.buttonState= ButtonState.Loading
                download()
            }

        }
        notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createChannel(
            getString(R.string.load_notification_channel_id),
            getString(R.string.load_notification_channel_name)
        )
    }


    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            if (downloadID == id) {

                if (intent.action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                    Timber.i("Finished")
                    custom_button.buttonState= ButtonState.Completed

                    if (context != null) {
                        notificationManager.sendNotification("Download completed","finished", PREDEFINED_URL_LIST.find { it.id == urlID }!!.text, context)
                    }
                }
            }
        }
    }

    private fun download() {
        val url_selected = PREDEFINED_URL_LIST.find { it.id == urlID }
        // Toast.makeText(applicationContext, "Download: ${url_selected?.url}", Toast.LENGTH_SHORT)
        //    .show()
        val request =
            DownloadManager.Request(Uri.parse(url_selected?.url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    private fun addRadioButtons() {
        val radioGroup = findViewById<RadioGroup>(R.id.url_radio_group)

        for (i in PREDEFINED_URL_LIST) {
            // Create RadioButton programmatically
            val radioButton = RadioButton(this)
            radioButton.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            radioButton.text = i.text
            radioButton.id = i.id
            radioGroup.addView(radioButton)
        }

    }
    companion object {
        private val PREDEFINED_URL_LIST = mutableListOf<Url>(
            Url(
                1,
                "GLIDE - Image Loading Library by BumpTech",
                "https://github.com/bumptech/glide/archive/refs/heads/master.zip"
            ),
            Url(
                2,
                "LoadApp - Current repository by Udacity",
                "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/refs/heads/master.zip"
            ),
            Url(
                3,
                "Retrofit - Type-safe HTTP client for Android and Java by Square, Inc",
                "https://github.com/square/retrofit/archive/refs/heads/master.zip"
            )
        )
    }
    private fun createChannel(channelId: String, channelName: String) {

        // Create a channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                // Change importance
                NotificationManager.IMPORTANCE_HIGH
            )// Disable badges for this channel
                .apply {
                    setShowBadge(false)
                }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = getString(R.string.app_name)

            val notificationManager = this.getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)

        }
    }

}
