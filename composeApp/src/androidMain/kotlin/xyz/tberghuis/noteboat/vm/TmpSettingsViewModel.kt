package xyz.tberghuis.noteboat.vm

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import java.io.File
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import xyz.tberghuis.noteboat.DB_FILENAME
import xyz.tberghuis.noteboat.IMPORT_DB_FILENAME
import xyz.tberghuis.noteboat.data.AppDatabase
import xyz.tberghuis.noteboat.data.NoteDao
import xyz.tberghuis.noteboat.data.PreferencesRepository
import xyz.tberghuis.noteboat.utils.logd

class TmpSettingsViewModel(
  private val application: Application,
  private val db: AppDatabase,
  private val preferencesRepository: PreferencesRepository,
  private val noteDao: NoteDao
) : ViewModel() {
  val showShortcutLockScreenFlow: Flow<Boolean>
    get() = preferencesRepository.showShortcutLockScreenFlow

  var confirmDeleteAllNotesDialog by mutableStateOf(false)

  // use kmp uri ....
  fun importDb(importDbUri: Uri) {
    viewModelScope.launch(IO) {
      val importDbFile = File(application.filesDir, IMPORT_DB_FILENAME)
      val inputStream = application.contentResolver.openInputStream(importDbUri)
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

  fun deleteAllNotes() {
    viewModelScope.launch(IO) {
      noteDao.deleteAll()
    }
  }

  fun showShortcutClick() {
    viewModelScope.launch {
      preferencesRepository.toggleShowShortcutLockScreen()
    }
  }


  // doitwrong, should do this with workmanager to be safe
  // from user canceling job
  fun backupDb(backupFileUri: Uri) {
    viewModelScope.launch(IO) {
      // checkpoint
      val query = "pragma wal_checkpoint(full)"
      db.query(query, null).use { cursor ->
        if (cursor.moveToFirst()) {
          val busy = cursor.getInt(0)
          val log = cursor.getInt(1)
          val checkpointed = cursor.getInt(2)
        }
      }

      val dbFile = application.getDatabasePath(DB_FILENAME)
      application.contentResolver.openOutputStream(backupFileUri)?.use { os ->
        dbFile.inputStream().use { fis ->
          fis.copyTo(os)
        }
      }
    }
  }
}