package xyz.tberghuis.noteboat.tmp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import xyz.tberghuis.noteboat.utils.logd

class TmpVm(val app: Application) : AndroidViewModel(app) {
  init {
    logd("TmpVm init")
  }
}