package xyz.tberghuis.noteboat.data

expect class AppDatabaseFactory {
  fun create(): AppDatabase
}