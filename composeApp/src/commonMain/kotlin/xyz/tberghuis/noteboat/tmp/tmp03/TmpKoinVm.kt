package xyz.tberghuis.noteboat.tmp.tmp03

import androidx.lifecycle.ViewModel
import xyz.tberghuis.noteboat.controller.SpeechControllerFactory
import xyz.tberghuis.noteboat.database.FruittieDao

class TmpKoinVm(
  val factory: SpeechControllerFactory,
  val dao: FruittieDao
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