package xyz.tberghuis.noteboat.tmp2

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import java.io.File
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import xyz.tberghuis.noteboat.DB_FILENAME
import xyz.tberghuis.noteboat.MainApplication
import xyz.tberghuis.noteboat.data.AppDatabase
import xyz.tberghuis.noteboat.data.Note
import xyz.tberghuis.noteboat.utils.logd

class TmpImportDbVm(
  application: Application,
) : AndroidViewModel(application) {
  var importFileUri: Uri? = null

  private val mainApp = (application as MainApplication)


  fun copyInputStream() {
    val newFile = File(mainApp.filesDir, "import-notes.db")
    val inputStream = mainApp.contentResolver.openInputStream(importFileUri!!)
    // https://www.baeldung.com/kotlin/inputstream-to-file
    inputStream!!.use { input ->
      newFile.outputStream().use { output ->
        input.copyTo(output)
      }
    }
  }

//  fun openAndCloseDb() {
//    viewModelScope.launch(IO) {
//      logd("stop db")
//      logd("isopen ${mainApp.appDatabase.isOpen}")
//      if (!mainApp.appDatabase.isOpen) {
//        mainApp.appDatabase.openHelper.readableDatabase
//        logd("isopen ${mainApp.appDatabase.isOpen}")
//      }
//      // deletes .db-shm and .db-wal files
//      mainApp.appDatabase.close()
//      logd("isopen ${mainApp.appDatabase.isOpen}")
//    }
//  }

//  fun importSelectedDb() {
//    val dbFile = mainApp.getDatabasePath(DB_FILENAME)
//    val inputStream = mainApp.contentResolver.openInputStream(importFileUri!!)
//    // https://www.baeldung.com/kotlin/inputstream-to-file
//    inputStream!!.use { input ->
//      dbFile.outputStream().use { output ->
//        input.copyTo(output)
//      }
//    }
//  }

  fun copyNotesFromTmpDb() {
    viewModelScope.launch(IO) {
      logd("copyNotesFromTmpDb")

      // open import-notes.db
      val importNotesFile = File(mainApp.filesDir, "import-notes.db")

      logd("importNotesFile ${importNotesFile.path}")

      // create room instance
      val roomImport = Room.databaseBuilder(
        mainApp,
        AppDatabase::class.java,
        importNotesFile.path
      )
        .build()

      // read all notes
      // if invalid file, room will log error and give me empty notes list
      val importNotesList = roomImport.noteDao().getAll().first().map {
        it.copy(noteId = 0)
      }
      // write to appDatabase
      mainApp.appDatabase.noteDao().insertAll(importNotesList)

      // close import db
      roomImport.close()

      // delete import db
      importNotesFile.delete()
    }
  }
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
