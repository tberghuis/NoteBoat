package xyz.tberghuis.noteboat.vm

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import xyz.tberghuis.noteboat.MainApplication
import xyz.tberghuis.noteboat.controller.SpeechController
import xyz.tberghuis.noteboat.utils.logd

class EditNoteViewModel(
  application: Application,
  savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {
  val noteId: Int = savedStateHandle.get<Int>("noteId")!!
  val noteDao = (application as MainApplication).appDatabase.noteDao()
  val noteTextFieldValueState = mutableStateOf(TextFieldValue())

  val transcribingStateFlow = MutableStateFlow(TranscribingState.NOT_TRANSCRIBING)
  val speechController =
    SpeechController(
      application,
      transcribingStateFlow,
      noteTextFieldValueState,
      ::updateNote
    )

  init {
    viewModelScope.launch {
      val noteText = noteDao.getNoteText(noteId)
      noteTextFieldValueState.value = TextFieldValue(noteText, TextRange(noteText.length))
      speechController.run()
    }
  }

  fun updateNote(noteText: String) {
    logd("updateNote $noteText")
    viewModelScope.launch {
      val oldNote = noteDao.getNoteText(noteId)
      if (oldNote == noteText) {
        return@launch
      }
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