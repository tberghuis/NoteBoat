package xyz.tberghuis.noteboat.data

import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import java.io.File
import xyz.tberghuis.noteboat.DB_FILENAME
import xyz.tberghuis.noteboat.utils.logd

actual class AppDatabaseFactory {
  actual fun create(): AppDatabase {
    val dbFile = File(System.getProperty("java.io.tmpdir"), DB_FILENAME)

    logd("db path ${dbFile.absolutePath}")

    return Room.databaseBuilder<AppDatabase>(
      name = dbFile.absolutePath,
    ).setDriver(BundledSQLiteDriver())
      .addCallback(
        object : RoomDatabase.Callback() {
          override suspend fun onCreate(connection: SQLiteConnection) {
            val query =
              "INSERT INTO option (option_key, option_value) VALUES( 'new_note_draft',	'');"

            connection.execSQL(query)
          }
        }
      )
      .build()
  }
}