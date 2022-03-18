package xyz.tberghuis.noteboat.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import xyz.tberghuis.noteboat.data.Note
import xyz.tberghuis.noteboat.data.NoteDao
import javax.inject.Inject
import kotlinx.coroutines.flow.collect

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val noteDao: NoteDao,
  ) : ViewModel() {

  val allNotes = noteDao.getAll()
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

//  private val _noteList = MutableStateFlow(listOf<Note>())
//  val noteList: StateFlow<List<Note>> = _noteList
//  init {
//    viewModelScope.launch {
//      // this is bad as will collect when activity DEAD
//      noteDao.getAll().collect {
//        _noteList.value = it
//      }
//    }
//  }
}