package xyz.tberghuis.noteboat.tmp.tmp01

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

  var categoryList by mutableStateOf("")


  init {
    viewModelScope.launch {
      categoryDao.getAll().collect {
        categoryList = it.toString()
      }
    }
  }


  fun insertCategories() {
    logd("insertCategories")

    viewModelScope.launch {
      categoryDao.insertAll(TmpCategory(1, "inbox", "blue"), TmpCategory(2, "apps", "red"))
    }
  }
}