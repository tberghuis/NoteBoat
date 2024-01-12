package xyz.tberghuis.noteboat.vm

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import xyz.tberghuis.noteboat.DB_FILENAME
import xyz.tberghuis.noteboat.MainApplication

class SettingsViewModel(
  val app: Application
) : AndroidViewModel(app) {
  private val preferencesRepository = (app as MainApplication).preferencesRepository
  val db = (app as MainApplication).appDatabase
  val showShortcutLockScreenFlow = preferencesRepository.showShortcutLockScreenFlow

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

      val dbFile = app.getDatabasePath(DB_FILENAME)
      app.contentResolver.openOutputStream(backupFileUri)?.use { os ->
        dbFile.inputStream().use { fis ->
          fis.copyTo(os)
        }
      }
    }
  }
}