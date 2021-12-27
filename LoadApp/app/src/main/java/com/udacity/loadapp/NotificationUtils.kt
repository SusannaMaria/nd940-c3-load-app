package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.udacity.loadapp.DetailActivity


// Extension function to send messages
/**
 * Builds and delivers the notification.
 *
 * @param context, activity context.
 */
fun NotificationManager.sendNotification(messageBody: String, status: String, filename: String, applicationContext: Context) {
    // Create the content intent for the notification, which launches
    // this activity
    // Create intent
    val contentIntent = Intent(applicationContext, DetailActivity::class.java)
        .putExtra(DetailActivity.EXTRA_DETAIL_STATUS, status)
        .putExtra(DetailActivity.EXTRA_DETAIL_FILENAME, filename)
    // Create PendingIntent
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT

    )

    // Get an instance of NotificationCompat.Builder
    // Build the notification
    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.load_notification_channel_id)
    )

        // Set title, text and icon to builder
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(applicationContext
            .getString(R.string.notification_title))
        .setContentText(messageBody)
        .setAutoCancel(true)
        // Set content intent
        .addAction(
            R.drawable.ic_assistant_black_24dp,
            applicationContext.getString(R.string.detail),
            contentPendingIntent
        )
        // Set priority
        .setPriority(NotificationCompat.PRIORITY_HIGH)
    // Call notify
    notify(NOTIFICATION_ID, builder.build())
}
