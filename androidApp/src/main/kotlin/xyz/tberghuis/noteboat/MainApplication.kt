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
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.compose.koinViewModel
import xyz.tberghuis.noteboat.data.PreferencesRepository
import xyz.tberghuis.noteboat.di.initKoin
import xyz.tberghuis.noteboat.receiver.ScreenReceiver
import xyz.tberghuis.noteboat.utils.logd

class MainApplication : Application() {
  private val screenReceiverIntentFilter = IntentFilter(Intent.ACTION_SCREEN_ON).apply {
    addAction(Intent.ACTION_SCREEN_OFF)
    addAction(Intent.ACTION_USER_PRESENT)
  }
  private val screenReceiver = ScreenReceiver()
  private val userPrefs: PreferencesRepository by inject()

  override fun onCreate() {
    super.onCreate()

    initKoin {
      androidContext(this@MainApplication)
    }

    channelLockScreen()
    registerScreenReceiver()
  }

  private fun registerScreenReceiver() {
    logd("registerScreenReceiver")

    CoroutineScope(IO).launch {

      userPrefs.showShortcutLockScreenFlow.collect { showShortcut ->
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