package xyz.tberghuis.noteboat.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
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

  val newNoteDraft: Flow<String>
    get() = optionDao.getOptionFlow("new_note_draft").map {
      it.optionValue
    }

  fun updateNewNoteDraft(newNoteDraft: String) {
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