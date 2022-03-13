package xyz.tberghuis.noteboat.vm

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import xyz.tberghuis.noteboat.controller.SpeechController
import xyz.tberghuis.noteboat.data.NoteDao
import xyz.tberghuis.noteboat.utils.logd
import javax.inject.Inject

@HiltViewModel
class EditNoteViewModel @Inject constructor(
  @ApplicationContext appContext: Context,
  private val noteDao: NoteDao,
  private val savedStateHandle: SavedStateHandle
) : ViewModel() {
  val noteId: Int = savedStateHandle.get<Int>("noteId")!!

  val noteTextFieldValueState = mutableStateOf(TextFieldValue())

  val transcribingStateFlow = MutableStateFlow(TranscribingState.NOT_TRANSCRIBING)
  val speechController =
    SpeechController(
      appContext,
      transcribingStateFlow,
      noteTextFieldValueState,
      ::updateNote
    )

  init {
    viewModelScope.launch {
      val noteText = withContext(Dispatchers.IO) {
        noteDao.getNoteText(noteId)
      }
      noteTextFieldValueState.value = TextFieldValue(noteText, TextRange(noteText.length))
      speechController.run()
    }
  }

  fun updateNote(noteText: String) {
    if (noteText == noteTextFieldValueState.value.text) {
      // nothing changed
      logd("updateNote nothing changed: $noteText")
      return
    }
    logd("updateNote $noteText")
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