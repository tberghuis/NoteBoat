package xyz.tberghuis.noteboat.vm

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import xyz.tberghuis.noteboat.controller.TranscribingState
import xyz.tberghuis.noteboat.data.NoteDao
import xyz.tberghuis.noteboat.tmp.tmp03.TmpSpeechControllerFactory
import xyz.tberghuis.noteboat.utils.logd

class EditNoteViewModel(
  savedStateHandle: SavedStateHandle,
  val noteDao: NoteDao,
  speechControllerFactory: TmpSpeechControllerFactory
) : ViewModel() {
  val noteId: Int = savedStateHandle.get<Int>("noteId")!!
//  val noteDao = application.appDatabase.noteDao()
  val noteTextFieldValueState = mutableStateOf(TextFieldValue())

  val transcribingStateFlow = MutableStateFlow(TranscribingState.NOT_TRANSCRIBING)
  val speechController =
    speechControllerFactory.create(
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

  @OptIn(ExperimentalTime::class)
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

  fun trashNote() {
    viewModelScope.launch {
//      noteDao.delete(noteId)
//      val n = noteDao.getNote(noteId).first()!!
      val n = noteDao.getNote(noteId)
      noteDao.update(n.copy(trash = true))
    }
  }
}