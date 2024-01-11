package xyz.tberghuis.noteboat.tmp

import android.app.Application
import android.database.Cursor
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import xyz.tberghuis.noteboat.MainApplication
import xyz.tberghuis.noteboat.utils.logd


class TmpVm(val app: Application) : AndroidViewModel(app) {
  val db = (app as MainApplication).appDatabase

  init {
    logd("TmpVm init")
  }

  fun closeDb() {
    logd("closeDb")
    db.close()
    logd("closeDb after close")
  }

  fun checkpoint() {
    logd("checkpoint")
    viewModelScope.launch(IO) {
      val query = "pragma wal_checkpoint(full)"
      val cursor: Cursor = db.query(query, null)
      if (cursor.moveToFirst()) {
        val busy = cursor.getInt(0)
        val log = cursor.getInt(1)
        val checkpointed = cursor.getInt(2)
      }
      cursor.close()
      db.close()
      logd("checkpoint finish")
    }
  }
}