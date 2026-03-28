package xyz.tberghuis.noteboat.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import xyz.tberghuis.noteboat.data.Note
import xyz.tberghuis.noteboat.data.NoteDao

class TrashScreenVm(
  private val noteDao: NoteDao
) :
  ViewModel() {
  //  private val noteDao = application.appDatabase.noteDao()
  val trashNotes = noteDao.getTrash()

  fun deleteNote(note: Note) {
    viewModelScope.launch {
      noteDao.delete(note)
    }
  }

  fun restoreNote(note: Note) {
    viewModelScope.launch {
      noteDao.update(note.copy(trash = false))
    }
  }
}