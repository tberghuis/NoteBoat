package xyz.tberghuis.noteboat.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import xyz.tberghuis.noteboat.data.Note
import xyz.tberghuis.noteboat.data.NoteDao
import xyz.tberghuis.noteboat.data.OptionDao
import javax.inject.Inject

@HiltViewModel
class NewNoteViewModel @Inject constructor(
  private val noteDao: NoteDao,
  private val optionDao: OptionDao,
) : ViewModel() {

  // if i was pedantic i could use null for initial

//  var partialResult by mutableStateOf("")
//  var baseTextFieldValue = TextFieldValue()
  var noteTextFieldValue by mutableStateOf(TextFieldValue())

  val transcribingStateFlow = MutableStateFlow(TranscribingState.NOT_TRANSCRIBING)

  init {
    viewModelScope.launch {
      val newNoteDraft = withContext(Dispatchers.IO) {
        optionDao.getOption("new_note_draft")
      }
//      baseTextFieldValue = TextFieldValue(newNoteDraft)
      noteTextFieldValue = TextFieldValue(newNoteDraft)
    }
  }

  fun updateNewNoteDraft(newNoteDraft: String) {
    // update tfv from here instead of from screen

    viewModelScope.launch {
      optionDao.updateOption("new_note_draft", newNoteDraft)
    }
  }

  fun saveNewNote(newNote: String) {
    // todo check trim newNoteText is not empty
    viewModelScope.launch {
      // todo epoch is that the right term???
      val epoch = Clock.System.now().toEpochMilliseconds()
      noteDao.insertAll(Note(noteText = newNote, createdEpoch = epoch, modifiedEpoch = epoch))
      optionDao.updateOption("new_note_draft", "")
    }
  }
}


enum class TranscribingState {
  TRANSCRIBING, NOT_TRANSCRIBING
}