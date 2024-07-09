package xyz.tberghuis.noteboat

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import xyz.tberghuis.noteboat.data.AppDatabase
import xyz.tberghuis.noteboat.data.PreferencesRepository
import xyz.tberghuis.noteboat.data.dataStore
import xyz.tberghuis.noteboat.receiver.ScreenReceiver
import xyz.tberghuis.noteboat.utils.logd

class MainApplication : Application() {

  lateinit var appDatabase: AppDatabase
  lateinit var preferencesRepository: PreferencesRepository

  private val screenReceiverIntentFilter = IntentFilter(Intent.ACTION_SCREEN_ON).apply {
    addAction(Intent.ACTION_SCREEN_OFF)
    addAction(Intent.ACTION_USER_PRESENT)
  }
  private val screenReceiver = ScreenReceiver()

  override fun onCreate() {
    super.onCreate()
    initializeDatabase()
    preferencesRepository = providePreferencesRepository()
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

  private fun initializeDatabase() {
    appDatabase = Room.databaseBuilder(
      this,
      AppDatabase::class.java,
      DB_FILENAME
    )
      .createFromAsset(DB_FILENAME)
      .build()
  }

  private fun providePreferencesRepository(): PreferencesRepository {
    return PreferencesRepository(dataStore)
  }
}