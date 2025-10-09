package xyz.tberghuis.noteboat.tmp.tmp02

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import xyz.tberghuis.noteboat.vm.HomeViewModel
import xyz.tberghuis.noteboat.composable.ActionsCard

@Composable
fun TmpHomeContent() {
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
      Box(
        Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
      ) {
        ActionsCard(note)
        TmpNoteCard(
          note,
          1000,
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
