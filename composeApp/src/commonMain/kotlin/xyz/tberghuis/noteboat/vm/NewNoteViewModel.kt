package xyz.tberghuis.noteboat.vm

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import xyz.tberghuis.noteboat.controller.TranscribingState
import xyz.tberghuis.noteboat.data.Note
import xyz.tberghuis.noteboat.data.NoteDao
import xyz.tberghuis.noteboat.data.OptionDao
import xyz.tberghuis.noteboat.tmp.tmp03.TmpSpeechControllerFactory
import xyz.tberghuis.noteboat.utils.logd

class NewNoteViewModel(
//  application: Application,
  savedStateHandle: SavedStateHandle,
  val noteDao: NoteDao,
  val optionDao: OptionDao,
  speechControllerFactory: TmpSpeechControllerFactory
) : ViewModel() {
  val navParam: String? = savedStateHandle.get<String>("navParam")
//  val noteDao = application.appDatabase.noteDao()
//  val optionDao = application.appDatabase.optionDao()

  // if i was pedantic i could use null for initial
  val noteTextFieldValueState = mutableStateOf(TextFieldValue())
  val transcribingStateFlow = MutableStateFlow(TranscribingState.NOT_TRANSCRIBING)
  val speechController =
    speechControllerFactory.create(
      transcribingStateFlow,
      noteTextFieldValueState,
      ::updateNewNoteDraft
    )

  init {

    logd("navParam $navParam")

    viewModelScope.launch {
      val newNoteDraft = withContext(Dispatchers.IO) {
        optionDao.getOption("new_note_draft")
      }
      noteTextFieldValueState.value = TextFieldValue(newNoteDraft, TextRange(newNoteDraft.length))
      speechController.run()
    }
    viewModelScope.launch {
      logd("NewNoteViewModel navParam $navParam")
      when (navParam) {
        "new_voice_note" -> {
          logd("new voice note")
          delay(3000L)
          logd("after delay 3000")
          // doitwrong don't bother checkSelfPermission for now
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

  @OptIn(ExperimentalTime::class)
  fun saveNewNote(newNote: String) {
    // todo check trim newNoteText is not empty
    viewModelScope.launch {
      // todo epoch is that the right term???
      val epoch = Clock.System.now().toEpochMilliseconds()
      noteDao.insertAll(Note(noteText = newNote, createdEpoch = epoch, modifiedEpoch = epoch))
      optionDao.updateOption("new_note_draft", "")
    }
    noteTextFieldValueState.value = TextFieldValue()
  }
}

