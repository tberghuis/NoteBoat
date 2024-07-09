package xyz.tberghuis.noteboat.vm

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import java.io.File
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import xyz.tberghuis.noteboat.DB_FILENAME
import xyz.tberghuis.noteboat.IMPORT_DB_FILENAME
import xyz.tberghuis.noteboat.MainApplication
import xyz.tberghuis.noteboat.data.AppDatabase
import xyz.tberghuis.noteboat.utils.logd

class SettingsViewModel(
  application: Application,
) : AndroidViewModel(application) {
  private val mainApp = (application as MainApplication)
  private val preferencesRepository = mainApp.preferencesRepository
  val db = mainApp.appDatabase
  val showShortcutLockScreenFlow = preferencesRepository.showShortcutLockScreenFlow

  var confirmDeleteAllNotesDialog by mutableStateOf(false)

  fun importDb(importDbUri: Uri) {
    viewModelScope.launch(IO) {
      // todo use CONSTANT for filename
      val importDbFile = File(mainApp.filesDir, IMPORT_DB_FILENAME)
      val inputStream = mainApp.contentResolver.openInputStream(importDbUri)
      // https://www.baeldung.com/kotlin/inputstream-to-file
      inputStream?.use { input ->
        importDbFile.outputStream().use { output ->
          input.copyTo(output)
        }
      }

      // open import-notes.db
      val importNotesFile = File(mainApp.filesDir, IMPORT_DB_FILENAME)
      logd("importNotesFile ${importNotesFile.path}")
      // create room instance
      val roomImport = Room.databaseBuilder(
        mainApp,
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
      mainApp.appDatabase.noteDao().insertAll(importNotesList)
      // close import db
      roomImport.close()
      // delete import db
      importNotesFile.delete()
    }
  }

  fun deleteAllNotes() {
    viewModelScope.launch(IO) {
      mainApp.appDatabase.noteDao().deleteAll()
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

      val dbFile = mainApp.getDatabasePath(DB_FILENAME)
      mainApp.contentResolver.openOutputStream(backupFileUri)?.use { os ->
        dbFile.inputStream().use { fis ->
          fis.copyTo(os)
        }
      }
    }
  }
}