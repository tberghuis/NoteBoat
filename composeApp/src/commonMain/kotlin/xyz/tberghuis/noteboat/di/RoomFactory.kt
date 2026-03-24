package xyz.tberghuis.noteboat.di

import xyz.tberghuis.noteboat.database.TmpAppDatabase

expect class RoomFactory {
    fun createRoomDatabase(): TmpAppDatabase

}