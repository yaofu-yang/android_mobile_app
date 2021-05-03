package edu.neu.mad_sea.yaofuyang.dual_tic_tac_toe_classes

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import edu.neu.mad_sea.yaofuyang.MainActivity
import edu.neu.mad_sea.yaofuyang.R
import edu.neu.mad_sea.yaofuyang.dual_tic_tac_toe_classes.DisplayTicTacToeMenuActivity.Companion.CHANNEL_NAME

class TicGameMessagingService : FirebaseMessagingService() {
    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.from)

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
        }

        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.notification!!.body)

            // Note: We happen to be just getting the body of the notification and displaying it.
            // We could also get the title and other info and do different things.
            sendNotification(remoteMessage.notification!!.body)
        }
    }
    // [END receive_message]
    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private fun sendNotification(messageBody: String?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder: Notification.Builder
        notificationBuilder = if (Build.VERSION.SDK_INT <= 25) {
            Notification.Builder(this)
                .setSmallIcon(R.drawable.portrait_photo)
                .setContentTitle("Firebase Message (Mod6)")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
        } else {
            Notification.Builder(this, CHANNEL_NAME)
                .setSmallIcon(R.drawable.portrait_photo)
                .setContentTitle("Firebase Message (Mod6) [26+]")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
        }
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    companion object {
        private val TAG = TicGameMessagingService::class.java.simpleName
    }
}