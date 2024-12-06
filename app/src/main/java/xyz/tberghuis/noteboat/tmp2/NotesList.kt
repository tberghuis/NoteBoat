package xyz.tberghuis.noteboat.tmp2

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun NotesList() {
  val viewModel: TrashScreenVm = viewModel()
  val trashNotes = viewModel.trashNotes.collectAsState(listOf())
  LazyColumn(
    contentPadding = PaddingValues(10.dp)
  ) {
    items(trashNotes.value,
      key = { it.hashCode() }
    ) { note ->
      Box(
        Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
      ) {
        Text("note $note")
//        ActionsCard(note)
//        NoteCard(
//          note,
//          offsetNotes.value.contains(note),
//          viewModel::onRevealActions,
//          viewModel::onHideActions
//        )
      }
    }
  }
}
