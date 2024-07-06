package xyz.tberghuis.noteboat.tmp2

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class TmpImportDbVm(
  val app: Application,
) : AndroidViewModel(app) {
  var importFileUri: Uri? = null

  fun copyInputStream() {
    // will this overwrite a file correctly?
    val newFile = File(app.filesDir, "newfile.db")
    val fos = FileOutputStream(newFile)
    val fis = app.contentResolver.openInputStream(importFileUri!!) as FileInputStream
    val inChannel = fis.channel
    val outChannel = fos.channel
    inChannel.transferTo(0, inChannel.size(), outChannel)
    fis.close()
    fos.close()
  }
}