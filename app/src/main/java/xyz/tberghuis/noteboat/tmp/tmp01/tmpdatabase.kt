package xyz.tberghuis.noteboat.tmp.tmp01

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
  entities = [TmpCategory::class, TmpNote::class], version = 1, exportSchema = false,
)
abstract class TmpDatabase : RoomDatabase() {

  companion object {
    @Volatile
    private var instance: TmpDatabase? = null
    fun getInstance(context: Context) =
      instance ?: synchronized(this) {
        instance ?: Room.databaseBuilder(
          context.applicationContext,
          TmpDatabase::class.java,
          "tmp.db"
        )
          .build()
          .also { instance = it }
      }
  }
}

val Context.tmpDatabase: TmpDatabase
  get() = TmpDatabase.getInstance(this)