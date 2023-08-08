package xyz.tberghuis.noteboat.receiver

import android.annotation.SuppressLint
import android.app.KeyguardManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import xyz.tberghuis.noteboat.LOCK_SCREEN_CHANNEL_ID
import xyz.tberghuis.noteboat.LOCK_SCREEN_EXTRA_START
import xyz.tberghuis.noteboat.LOCK_SCREEN_NOTIFICATION_ID
import xyz.tberghuis.noteboat.MainActivity
import xyz.tberghuis.noteboat.R
import xyz.tberghuis.noteboat.utils.logd

class ScreenReceiver : BroadcastReceiver() {

  override fun onReceive(context: Context?, intent: Intent?) {
    logd("ScreenOnReceiver onReceive")

    if (context == null || intent?.action == null) {
      return
    }

    when (intent.action) {
      Intent.ACTION_SCREEN_ON -> {
        val keyguardManager: KeyguardManager =
          context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

        if (keyguardManager.isKeyguardLocked) {
          postNotification(context)
        }
      }

      Intent.ACTION_SCREEN_OFF, Intent.ACTION_USER_PRESENT -> {
        logd("action ${intent.action}")
        NotificationManagerCompat.from(context).cancel(LOCK_SCREEN_NOTIFICATION_ID)
      }
    }

  }
}

@SuppressLint("MissingPermission")
private fun postNotification(context: Context) {

  val intent = Intent(Intent.ACTION_VIEW).apply {
    data = "noteboat://noteboat/new-note/new_voice_note".toUri()
    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    putExtra(LOCK_SCREEN_EXTRA_START, true)
  }

  val pendingIntent: PendingIntent =
    PendingIntent.getActivity(
      context,
      System.currentTimeMillis().toInt(),
      intent,
      PendingIntent.FLAG_IMMUTABLE
    )

  val builder = NotificationCompat.Builder(context, LOCK_SCREEN_CHANNEL_ID)
    .setSmallIcon(R.drawable.ic_launcher_foreground)
    .setContentTitle("content title")
    .setContentText("content text")
    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    .setContentIntent(pendingIntent)
    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
    .setAutoCancel(true)

  with(NotificationManagerCompat.from(context)) {
    // notificationId is a unique int for each notification that you must define
    notify(LOCK_SCREEN_NOTIFICATION_ID, builder.build())
  }
}



