package xyz.tberghuis.noteboat.tmp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import xyz.tberghuis.noteboat.utils.logd


class TmpVm(application: Application) : AndroidViewModel(application) {

  init {
    logd("TmpVm init")
  }

  fun closeDb() {
    logd("closeDb")
  }
}