package xyz.tberghuis.noteboat.di

import android.app.Application
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import xyz.tberghuis.noteboat.database.DB_FILE_NAME
import xyz.tberghuis.noteboat.database.TmpAppDatabase

actual class RoomFactory(
  private val app: Application,
) {
  actual fun createRoomDatabase(): TmpAppDatabase {
    val dbFile = app.getDatabasePath(DB_FILE_NAME)
    return Room
      .databaseBuilder<TmpAppDatabase>(
        context = app,
        name = dbFile.absolutePath,
      ).setDriver(BundledSQLiteDriver())
      .setQueryCoroutineContext(Dispatchers.IO)
      .build()
  }
}
