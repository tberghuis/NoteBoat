package xyz.tberghuis.noteboat.data

import android.app.Application
import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import xyz.tberghuis.noteboat.DB_FILENAME

@Database(
  entities = [Note::class, Option::class], version = 3, exportSchema = true,
  autoMigrations = [
    AutoMigration(from = 1, to = 2),
    AutoMigration(from = 2, to = 3)
  ]
)
abstract class AppDatabase : RoomDatabase() {
  abstract fun noteDao(): NoteDao
  abstract fun optionDao(): OptionDao

  companion object {
    @Volatile
    private var instance: AppDatabase? = null
    fun getInstance(application: Application) =
      instance ?: synchronized(this) {
        instance ?: Room.databaseBuilder(
          application,
          AppDatabase::class.java,
          DB_FILENAME
        )
          .createFromAsset(DB_FILENAME)
          .build()
          .also { instance = it }
      }
  }
}

val Context.appDatabase: AppDatabase
  get() = AppDatabase.getInstance(this.applicationContext as Application)