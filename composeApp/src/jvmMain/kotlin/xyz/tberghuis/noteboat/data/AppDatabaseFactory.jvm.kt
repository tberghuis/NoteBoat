package xyz.tberghuis.noteboat.data

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import java.io.File
import xyz.tberghuis.noteboat.DATASTORE_FILENAME
import xyz.tberghuis.noteboat.DB_FILENAME

actual class AppDatabaseFactory {
  actual fun create(): AppDatabase {
    val dbFile = File(System.getProperty("java.io.tmpdir"), DB_FILENAME)

    println("db path ${dbFile.absolutePath}")

    return Room.databaseBuilder<AppDatabase>(
      name = dbFile.absolutePath,
    ).setDriver(BundledSQLiteDriver()).build()
  }
}