package xyz.tberghuis.noteboat.vm

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room3.Room
import androidx.room3.useWriterConnection
import java.io.File
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import xyz.tberghuis.noteboat.DB_FILENAME
import xyz.tberghuis.noteboat.IMPORT_DB_FILENAME
import xyz.tberghuis.noteboat.data.AppDatabase
import xyz.tberghuis.noteboat.data.BackupDbFunFactory
import xyz.tberghuis.noteboat.data.NoteDao
import xyz.tberghuis.noteboat.data.PreferencesRepository
import xyz.tberghuis.noteboat.utils.logd

class SettingsViewModel(
  private val preferencesRepository: PreferencesRepository,
  private val noteDao: NoteDao,
  private val _backupDb: suspend (backupFileUri: String) -> Unit,
  private val _importDb: suspend (importDbUri: String) -> Unit
) : ViewModel() {
  val showShortcutLockScreenFlow: Flow<Boolean>
    get() = preferencesRepository.showShortcutLockScreenFlow

  var confirmDeleteAllNotesDialog by mutableStateOf(false)

  // use kmp uri ....
  fun importDb(importDbUri: String) {
    viewModelScope.launch(IO) {
      _importDb(importDbUri)
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
  fun backupDb(backupFileUri: String) {
    viewModelScope.launch(IO) {
      _backupDb(backupFileUri)
    }
  }
}