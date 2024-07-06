package xyz.tberghuis.noteboat.tmp2

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import xyz.tberghuis.noteboat.utils.logd


class TmpImportDbVm(
  application: Application,
) : AndroidViewModel(application) {

  var importFileUri: Uri? = null
}
