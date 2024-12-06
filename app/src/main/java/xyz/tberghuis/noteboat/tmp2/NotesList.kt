package xyz.tberghuis.noteboat.tmp2

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun TrashNotesList() {
  val viewModel: TrashScreenVm = viewModel()
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