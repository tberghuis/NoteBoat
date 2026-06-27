package xyz.tberghuis.noteboat.data

import androidx.room3.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import java.io.File
import kotlinx.coroutines.flow.first

actual class ImportDbFunFactory(
  private val noteDao: NoteDao,
) {
  actual fun create(): suspend (importDbUri: String) -> Unit {
    return { importDbUri ->
      val roomImport = Room.databaseBuilder<AppDatabase>(
        name = File(importDbUri).absolutePath
      ).setDriver(BundledSQLiteDriver())
        .build()
      // read all notes
      // if invalid file, room will log error and give me empty notes list
      val importNotesList = roomImport.noteDao().getAll().first().map {
        it.copy(noteId = 0)
      }
      // write to appDatabase
      noteDao.insertAll(importNotesList)
      // close import db
      roomImport.close()
    }
  }
}