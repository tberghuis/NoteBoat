package xyz.tberghuis.noteboat.database

import xyz.tberghuis.noteboat.data.Fruittie

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor

@Database(entities = [Fruittie::class], version = 1)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class TmpAppDatabase : RoomDatabase() {
    abstract fun fruittieDao(): TmpFruittieDao
}

// The Room compiler generates the `actual` implementations.
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<TmpAppDatabase> {
    override fun initialize(): TmpAppDatabase
}

internal const val DB_FILE_NAME = "fruits.db"
