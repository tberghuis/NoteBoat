package xyz.tberghuis.noteboat.tmp.tmp01

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import xyz.tberghuis.noteboat.utils.logd

class TmpVm(
  application: Application,
) :
  AndroidViewModel(application) {

  fun insertCategories(){
    logd("insertCategories")
  }
}