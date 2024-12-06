package xyz.tberghuis.noteboat.tmp2

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import xyz.tberghuis.noteboat.data.Note
import xyz.tberghuis.noteboat.data.appDatabase

class TrashScreenVm(
  application: Application,
//  savedStateHandle: SavedStateHandle
) :
  AndroidViewModel(application) {

  private val noteDao = application.appDatabase.noteDao()
  // todo
  val trashNotes = noteDao.getTrash()

  fun deleteNote(note: Note) {
    viewModelScope.launch {
      noteDao.delete(note)
    }
  }
}