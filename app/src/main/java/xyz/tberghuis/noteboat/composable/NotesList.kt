package xyz.tberghuis.noteboat.composable

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import xyz.tberghuis.noteboat.tmp2.TrashNoteCard
import xyz.tberghuis.noteboat.tmp2.TrashScreenVm

@Composable
fun TrashNotesList(
    viewModel: TrashScreenVm = viewModel()
) {
  val trashNotes = viewModel.trashNotes.collectAsState(listOf())
  LazyColumn(
    contentPadding = PaddingValues(10.dp)
  ) {
    items(trashNotes.value,
      key = { it.hashCode() }
    ) { note ->
      TrashNoteCard(note)
    }
  }
}