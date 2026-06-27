package xyz.tberghuis.noteboat.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import xyz.tberghuis.noteboat.data.NoteDao
import xyz.tberghuis.noteboat.data.PreferencesRepository

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