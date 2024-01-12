package xyz.tberghuis.noteboat.tmp

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import xyz.tberghuis.noteboat.MainApplication
import xyz.tberghuis.noteboat.utils.logd

class TmpVm(val app: Application) : AndroidViewModel(app) {
  val db = (app as MainApplication).appDatabase
  var uri by mutableStateOf<Uri?>(null)

  init {
    logd("TmpVm init")
  }

  fun checkpoint() {
    logd("checkpoint")
    viewModelScope.launch(IO) {
      val query = "pragma wal_checkpoint(full)"
      db.query(query, null).use { cursor ->
        if (cursor.moveToFirst()) {
          val busy = cursor.getInt(0)
          val log = cursor.getInt(1)
          val checkpointed = cursor.getInt(2)
        }
      }
      // no need to close DB
      logd("checkpoint finish")
    }
  }

  fun writeDbFile() {
    logd("writeDbFile")
    // todo database filename = DB_FILENAME
    val dbFile = app.getDatabasePath("noteboatv2.db")
    app.contentResolver.openOutputStream(uri!!)?.use { os ->
      dbFile.inputStream().use { fis ->
        fis.copyTo(os)
      }
    }
  }
}