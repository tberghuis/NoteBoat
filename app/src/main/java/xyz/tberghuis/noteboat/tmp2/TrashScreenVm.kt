package xyz.tberghuis.noteboat.tmp2

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import xyz.tberghuis.noteboat.MainApplication

class TrashScreenVm(
  application: Application,
  savedStateHandle: SavedStateHandle
) :
  AndroidViewModel(application) {

  val noteDao = (application as MainApplication).appDatabase.noteDao()
  // todo
  val trashNotes = noteDao.getAll()


}