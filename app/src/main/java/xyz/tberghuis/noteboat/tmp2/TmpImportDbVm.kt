package xyz.tberghuis.noteboat.tmp2

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class TmpImportDbVm(
  val app: Application,
) : AndroidViewModel(app) {
  var importFileUri: Uri? = null

  fun copyInputStream() {
    val newFile = File(app.filesDir, "newfile.db")
    val inputStream = app.contentResolver.openInputStream(importFileUri!!)
    copyStreamToFile(inputStream!!, newFile)
  }
}

// https://stackoverflow.com/questions/10854211/android-store-inputstream-in-file
// will this overwrite a file correctly?
fun copyStreamToFile(inputStream: InputStream, outputFile: File) {
  inputStream.use { input ->
    val outputStream = FileOutputStream(outputFile)
    outputStream.use { output ->
      val buffer = ByteArray(4 * 1024) // buffer size
      while (true) {
        val byteCount = input.read(buffer)
        if (byteCount < 0) break
        output.write(buffer, 0, byteCount)
      }
      output.flush()
    }
  }
}

