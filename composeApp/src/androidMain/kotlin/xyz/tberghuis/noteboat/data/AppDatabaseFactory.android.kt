package xyz.tberghuis.noteboat.data

import android.app.Application
import androidx.room3.Room
import kotlinx.coroutines.Dispatchers
import xyz.tberghuis.noteboat.DB_FILENAME

actual class AppDatabaseFactory(
  private val app: Application,
) {
  actual fun create(): AppDatabase {
//    val dbFile = app.getDatabasePath(DB_FILENAME)
//    return Room
//      .databaseBuilder<AppDatabase>(
//        context = app,
//        name = dbFile.absolutePath,
//      ).setDriver(BundledSQLiteDriver())
//      .setQueryCoroutineContext(Dispatchers.IO)
//      .build()


    return Room.databaseBuilder(
      app,
      AppDatabase::class.java,
      DB_FILENAME
    )
//      .setDriver(BundledSQLiteDriver())
      .setQueryCoroutineContext(Dispatchers.IO)
      .createFromAsset(DB_FILENAME)
      .build()

  }
}