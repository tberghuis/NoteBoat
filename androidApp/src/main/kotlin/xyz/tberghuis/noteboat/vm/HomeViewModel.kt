package xyz.tberghuis.noteboat.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import xyz.tberghuis.noteboat.data.Note
import xyz.tberghuis.noteboat.data.appDatabase

class HomeViewModel(
  application: Application,
) : AndroidViewModel(application) {
  val noteDao = application.appDatabase.noteDao()
  val allNotes = noteDao.getAll().stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5000L), // Start sharing while subscribed, stop after 5s if no subscribers
    listOf()
  )
  // https://github.com/Skyyo/compose-swipe-to-reveal
  val offsetNotes = MutableStateFlow(setOf<Note>())

  fun trashNote(note: Note) {
    viewModelScope.launch {
//      noteDao.delete(note)
      noteDao.update(note.copy(trash = true))
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
    viewModelScope.launch {
      noteDao.update(note.copy(pinned = !note.pinned))
    }
  }
}