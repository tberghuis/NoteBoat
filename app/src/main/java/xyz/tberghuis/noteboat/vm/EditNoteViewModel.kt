package xyz.tberghuis.noteboat.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import xyz.tberghuis.noteboat.data.NoteDao
import javax.inject.Inject

@HiltViewModel
class EditNoteViewModel @Inject constructor(
  private val noteDao: NoteDao,
  private val savedStateHandle: SavedStateHandle
) : ViewModel() {
  val noteId: Int = savedStateHandle.get<Int>("noteId")!!

  val noteText: Flow<String>
    get() = noteDao.getNote(noteId)
      .mapNotNull {
        it?.noteText
      }

  fun updateNote(noteText: String) {
    viewModelScope.launch {

      val epoch = Clock.System.now().toEpochMilliseconds()
      noteDao.updateNoteText(noteId, noteText, epoch)
    }
  }

  fun deleteNote() {
    viewModelScope.launch {
      noteDao.delete(noteId)
    }
  }

}