package xyz.tberghuis.noteboat.tmp2

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import xyz.tberghuis.noteboat.DB_FILENAME
import xyz.tberghuis.noteboat.MainApplication
import xyz.tberghuis.noteboat.utils.logd

class TmpImportDbVm(
  application: Application,
) : AndroidViewModel(application) {
  var importFileUri: Uri? = null

  private val mainApp = (application as MainApplication)


  fun copyInputStream() {
    val newFile = File(mainApp.filesDir, "newfile.db")
    val inputStream = mainApp.contentResolver.openInputStream(importFileUri!!)
    // https://www.baeldung.com/kotlin/inputstream-to-file
    inputStream!!.use { input ->
      newFile.outputStream().use { output ->
        input.copyTo(output)
      }
    }
  }

  fun openAndCloseDb() {
    viewModelScope.launch(IO) {
      logd("stop db")
      logd("isopen ${mainApp.appDatabase.isOpen}")
      if (!mainApp.appDatabase.isOpen) {
        mainApp.appDatabase.openHelper.readableDatabase
        logd("isopen ${mainApp.appDatabase.isOpen}")
      }
      // deletes .db-shm and .db-wal files
      mainApp.appDatabase.close()
      logd("isopen ${mainApp.appDatabase.isOpen}")
    }
  }

  fun importSelectedDb() {
    val dbFile = mainApp.getDatabasePath(DB_FILENAME)
    val inputStream = mainApp.contentResolver.openInputStream(importFileUri!!)
    // https://www.baeldung.com/kotlin/inputstream-to-file
    inputStream!!.use { input ->
      dbFile.outputStream().use { output ->
        input.copyTo(output)
      }
    }
  }

  // willitblend
//  fun reloadDb() {
//    logd("reload db")
//    mainApp.initializeDatabase(false)
//    mainApp.appDatabase.openHelper.writableDatabase
//  }


}

// https://stackoverflow.com/questions/10854211/android-store-inputstream-in-file
// will this overwrite a file correctly?
//fun copyStreamToFile(inputStream: InputStream, outputFile: File) {
//  inputStream.use { input ->
//    val outputStream = FileOutputStream(outputFile)
//    outputStream.use { output ->
//      val buffer = ByteArray(4 * 1024) // buffer size
//      while (true) {
//        val byteCount = input.read(buffer)
//        if (byteCount < 0) break
//        output.write(buffer, 0, byteCount)
//      }
//      output.flush()
//    }
//  }
//}
