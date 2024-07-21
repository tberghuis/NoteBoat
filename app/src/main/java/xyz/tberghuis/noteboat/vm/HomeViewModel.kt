package xyz.tberghuis.noteboat.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import xyz.tberghuis.noteboat.MainApplication
import xyz.tberghuis.noteboat.data.Note

class HomeViewModel(
  application: Application,
) : AndroidViewModel(application) {
  val noteDao = (application as MainApplication).appDatabase.noteDao()
  val allNotes = noteDao.getAll()

  // https://github.com/Skyyo/compose-swipe-to-reveal
  val offsetNotes = MutableStateFlow(setOf<Note>())

  fun deleteNote(note: Note) {
    viewModelScope.launch {
      noteDao.delete(note)
      offsetNotes.value -= note
    }
  }

  fun onRevealActions(note: Note) {
    offsetNotes.value += note
  }

  fun onHideActions(note: Note) {
    offsetNotes.value -= note
  }

  fun togglePinned(note: Note) {
    offsetNotes.value -= note

//    note.pinned = !note.pinned

    viewModelScope.launch {
      noteDao.update(note.copy(pinned = !note.pinned))
//      noteDao.update(note)
    }
  }
}