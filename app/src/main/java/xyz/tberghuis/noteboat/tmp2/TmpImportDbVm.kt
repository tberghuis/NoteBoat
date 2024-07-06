package xyz.tberghuis.noteboat.tmp2

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import java.io.File
import xyz.tberghuis.noteboat.utils.logd


class TmpImportDbVm(
  val app: Application,
) : AndroidViewModel(app) {

  var importFileUri: Uri? = null

  var newFile: File? = null


  fun createNewFile() {
    newFile = File(app.filesDir, "newfile.db")
//    newFile?.createNewFile()
  }
  

}
