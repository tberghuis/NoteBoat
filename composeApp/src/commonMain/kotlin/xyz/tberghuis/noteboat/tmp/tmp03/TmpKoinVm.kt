package xyz.tberghuis.noteboat.tmp.tmp03

import androidx.lifecycle.ViewModel

class TmpKoinVm(): ViewModel() {
    init {
        println("Home TmpKoinVm initializing...")
    }

    override fun onCleared() {
        super.onCleared()
        println("Home TmpKoinVm clearing...")
    }
}