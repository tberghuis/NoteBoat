package xyz.tberghuis.noteboat.di

import xyz.tberghuis.noteboat.data.AppDatabase
import xyz.tberghuis.noteboat.database.TmpAppDatabase

expect class RoomFactory {
  fun createRoomDatabase(): TmpAppDatabase

  fun createAppDatabase(): AppDatabase
}