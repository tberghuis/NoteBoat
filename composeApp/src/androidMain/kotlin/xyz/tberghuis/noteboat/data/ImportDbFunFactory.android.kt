package xyz.tberghuis.noteboat.data

import android.app.Application
import androidx.core.net.toUri
import androidx.room3.Room
import java.io.File
import kotlinx.coroutines.flow.first
import xyz.tberghuis.noteboat.IMPORT_DB_FILENAME
import xyz.tberghuis.noteboat.utils.logd

actual class ImportDbFunFactory(
  private val application: Application,
  private val noteDao: NoteDao,
) {
  actual fun create(): suspend (importDbUri: String) -> Unit {
    return { importDbUri ->
      val importDbFile = File(application.filesDir, IMPORT_DB_FILENAME)
      val inputStream = application.contentResolver.openInputStream(importDbUri.toUri())
      // https://www.baeldung.com/kotlin/inputstream-to-file
      inputStream?.use { input ->
        importDbFile.outputStream().use { output ->
          input.copyTo(output)
        }
      }

      // open import-notes.db
      val importNotesFile = File(application.filesDir, IMPORT_DB_FILENAME)
      logd("importNotesFile ${importNotesFile.path}")
      // create room instance
      val roomImport = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        importNotesFile.path
      )
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
      // delete import db
      importNotesFile.delete()

    }
  }
}