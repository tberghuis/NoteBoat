package xyz.tberghuis.noteboat.tmp

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.database.Cursor
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import xyz.tberghuis.noteboat.MainApplication
import xyz.tberghuis.noteboat.utils.logd


val TMP_WRITE_REQUEST_CODE = 111

class TmpVm(val app: Application) : AndroidViewModel(app) {
  val db = (app as MainApplication).appDatabase

  var path by mutableStateOf<String?>(null)


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


  fun createFile(activity: Activity) {
//    val mimeType = "application/x-sqlite3"
    val mimeType = "text/*"
    val fileName = "willitblend.txt"

    val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
    intent.addCategory(Intent.CATEGORY_OPENABLE)
    intent.setType(mimeType)
    intent.putExtra(Intent.EXTRA_TITLE, fileName)
    startActivityForResult(activity, intent, TMP_WRITE_REQUEST_CODE, null)
  }


  fun writeTxtFile() {
    logd("writeTxtFile")
    val fos = app.contentResolver.openOutputStream()

  }

  fun writeTxtFileTmp() {
    val fileOutupStream: FileOutputStream? = contentResolver.openOutputStream(data.data!!)
    try {
      bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutupStream)
      fileOutupStream!!.flush()
      fileOutupStream.close()
      Toast.makeText(this, "saved $fileName", Toast.LENGTH_LONG).show()
    } catch (e: Exception) {
      Toast.makeText(this, "something went wrong" + e.message, Toast.LENGTH_SHORT).show()
      e.printStackTrace()
    }

  }


}