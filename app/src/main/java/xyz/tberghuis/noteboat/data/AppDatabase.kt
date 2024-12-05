package xyz.tberghuis.noteboat.data

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase

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
}