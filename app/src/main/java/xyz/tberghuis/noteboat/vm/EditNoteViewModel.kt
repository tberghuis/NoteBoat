package xyz.tberghuis.noteboat.vm

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import xyz.tberghuis.noteboat.data.NoteDao
import javax.inject.Inject

@HiltViewModel
class EditNoteViewModel @Inject constructor(
  private val noteDao: NoteDao,
  private val savedStateHandle: SavedStateHandle
) : ViewModel() {
  val noteId: Int = savedStateHandle.get<Int>("noteId")!!

//  val noteText: Flow<String?>
//    get() = noteDao.getNote(noteId)
//      .mapNotNull {
//        it?.noteText
//      }

  val noteTextFieldValueState = mutableStateOf(TextFieldValue())

  init {
    viewModelScope.launch {
      val noteText = withContext(Dispatchers.IO) {
        noteDao.getNoteText(noteId)
      }
      noteTextFieldValueState.value = TextFieldValue(noteText)
//      speechController.run()
    }
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