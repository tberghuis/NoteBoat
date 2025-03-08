package xyz.tberghuis.noteboat.tmp.tmp01

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import xyz.tberghuis.noteboat.utils.logd

class TmpVm(
  application: Application,
) :
  AndroidViewModel(application) {
  val categoryDao = application.tmpDatabase.categoryDao()
  val tmpNoteDao = application.tmpDatabase.tmpNoteDao()

  fun insertCategories() {
    logd("insertCategories")

    viewModelScope.launch {

    }
  }
}