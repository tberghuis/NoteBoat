package xyz.tberghuis.noteboat.tmp.tmp03

import androidx.lifecycle.ViewModel

class TmpKoinVm(
  val factory: TmpSpeechControllerFactory
) : ViewModel() {


  val willitblend = "will it blend"

  init {
    println("Home TmpKoinVm initializing...")
  }

  fun factoryCreate() {
//    println(factory.create())
  }


  override fun onCleared() {
    super.onCleared()
    println("Home TmpKoinVm clearing...")
  }
}