package xyz.tberghuis.noteboat.data

import android.app.Application
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import xyz.tberghuis.noteboat.DB_FILENAME

actual class AppDatabaseFactory(
  private val app: Application,
) {
  actual fun create(): AppDatabase {
    val dbFile = app.getDatabasePath(DB_FILENAME)
    return Room
      .databaseBuilder<AppDatabase>(
        context = app,
        name = dbFile.absolutePath,
      ).setDriver(BundledSQLiteDriver())
      .setQueryCoroutineContext(Dispatchers.IO)
      .build()

  }
}