package xyz.tberghuis.noteboat

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.IntentFilter
import dagger.hilt.android.HiltAndroidApp
import xyz.tberghuis.noteboat.receiver.ScreenReceiver
import xyz.tberghuis.noteboat.utils.logd

@HiltAndroidApp
class MainApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    channelLockScreen()
    registerScreenReceiver()
  }

  private fun registerScreenReceiver() {
    logd("registerScreenReceiver")
    val screenReceiver = ScreenReceiver()
    val intentFilter = IntentFilter(Intent.ACTION_SCREEN_ON)
    intentFilter.addAction(Intent.ACTION_SCREEN_OFF)
    intentFilter.addAction(Intent.ACTION_USER_PRESENT)
    registerReceiver(screenReceiver, intentFilter)
  }


  private fun channelLockScreen() {
    val importance = NotificationManager.IMPORTANCE_DEFAULT
    val mChannel = NotificationChannel(
      LOCK_SCREEN_CHANNEL_ID,
      "todo lockscreen channel name strings.xml",
      importance
    )
    mChannel.description = "todo lockscreen channel desc strings.xml"
    mChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
    val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(mChannel)
  }

}