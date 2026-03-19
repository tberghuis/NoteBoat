package xyz.tberghuis.noteboat

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import xyz.tberghuis.noteboat.data.preferencesRepository
import xyz.tberghuis.noteboat.receiver.ScreenReceiver
import xyz.tberghuis.noteboat.utils.logd

class MainApplication : Application() {
  private val screenReceiverIntentFilter = IntentFilter(Intent.ACTION_SCREEN_ON).apply {
    addAction(Intent.ACTION_SCREEN_OFF)
    addAction(Intent.ACTION_USER_PRESENT)
  }
  private val screenReceiver = ScreenReceiver()

  override fun onCreate() {
    super.onCreate()
    channelLockScreen()
    registerScreenReceiver()
  }

  private fun registerScreenReceiver() {
    logd("registerScreenReceiver")

    CoroutineScope(IO).launch {
      preferencesRepository.showShortcutLockScreenFlow.collect { showShortcut ->
        if (showShortcut) {
          registerReceiver(screenReceiver, screenReceiverIntentFilter)
        } else {
          try {
            unregisterReceiver(screenReceiver)
          } catch (e: IllegalArgumentException) {
            Log.e("MainApplication", "$e")
          }
        }
      }
    }
  }

  private fun channelLockScreen() {
    val importance = NotificationManager.IMPORTANCE_DEFAULT
    val mChannel = NotificationChannel(
      LOCK_SCREEN_CHANNEL_ID,
      "Lock screen shortcut: New voice note",
      importance
    )
    mChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
    val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(mChannel)
  }
}