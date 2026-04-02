package xyz.tberghuis.noteboat.di

import xyz.tberghuis.noteboat.data.AppDatabase
import xyz.tberghuis.noteboat.database.TmpAppDatabase

actual class RoomFactory {
  actual fun createRoomDatabase(): TmpAppDatabase {
    TODO("Not yet implemented")
  }

  actual fun createAppDatabase(): AppDatabase {
    TODO("Not yet implemented")
  }
}