package xyz.tberghuis.noteboat.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import xyz.tberghuis.noteboat.vm.HomeViewModel
import xyz.tberghuis.noteboat.data.Note

@Composable
fun HomeContent() {
  val viewModel: HomeViewModel = viewModel()
  val allNotes = viewModel.allNotes.collectAsState(listOf())
  val offsetNotes = viewModel.offsetNotes.collectAsState(setOf())
  LazyColumn(
    contentPadding = PaddingValues(10.dp)
  ) {
    // when key = note_id there was a bug, could not swipe to reveal after toggling note.pinned
    itemsIndexed(
      items = allNotes.value,
      key = { index, item ->
        item.hashCode()
      }
    ) { index, note ->
      // calc note number
      val noteNumber = calcNoteNumber(note, index, allNotes.value.size)
      Box(
        Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
      ) {
        ActionsCard(note)
        NoteCard(
          note,
          noteNumber,
          offsetNotes.value.contains(note),
          viewModel::onRevealActions,
          viewModel::onHideActions
        )
      }
    }
    item {
      // clear FAB
      Spacer(Modifier.height(70.dp))
    }
  }
}

private fun calcNoteNumber(note: Note, index: Int, totalNotes: Int): Int? {
  if (note.pinned) {
    return null
  }
  return totalNotes - index
}