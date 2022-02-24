package xyz.tberghuis.noteboat.vm

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import xyz.tberghuis.noteboat.controller.SpeechController
import xyz.tberghuis.noteboat.data.Note
import xyz.tberghuis.noteboat.data.NoteDao
import xyz.tberghuis.noteboat.data.OptionDao
import xyz.tberghuis.noteboat.utils.logd
import javax.inject.Inject

@HiltViewModel
class NewNoteViewModel @Inject constructor(
  @ApplicationContext appContext: Context,
  private val noteDao: NoteDao,
  private val optionDao: OptionDao,
  savedStateHandle: SavedStateHandle
) : ViewModel() {
  val navParam: String? = savedStateHandle.get<String>("navParam")

  // if i was pedantic i could use null for initial
  val noteTextFieldValueState = mutableStateOf(TextFieldValue())
  val transcribingStateFlow = MutableStateFlow(TranscribingState.NOT_TRANSCRIBING)
  val speechController =
    SpeechController(
      appContext,
      transcribingStateFlow,
      noteTextFieldValueState,
      ::updateNewNoteDraft
    )

  init {
    viewModelScope.launch {
      val newNoteDraft = withContext(Dispatchers.IO) {
        optionDao.getOption("new_note_draft")
      }
      noteTextFieldValueState.value = TextFieldValue(newNoteDraft)
      speechController.run()
    }
    viewModelScope.launch {
      when (navParam) {
        "voice" -> {
          logd("new voice note")
          delay(3000L)
          logd("after delay 3000")
          // doitwrong don't bother checkSelfPermission
          transcribingStateFlow.value = TranscribingState.TRANSCRIBING
        }
      }
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