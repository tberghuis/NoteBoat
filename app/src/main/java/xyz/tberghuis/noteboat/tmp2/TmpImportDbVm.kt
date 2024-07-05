package xyz.tberghuis.noteboat.tmp2

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import xyz.tberghuis.noteboat.utils.logd


class TmpImportDbVm(
  application: Application,
) : AndroidViewModel(application) {

  fun filePicker() {
    logd("file picker")
  }
}