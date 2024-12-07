package xyz.tberghuis.noteboat.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.ui.unit.dp
import xyz.tberghuis.noteboat.data.Note
import androidx.compose.material.Text
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import xyz.tberghuis.noteboat.utils.logd
import androidx.lifecycle.viewmodel.compose.viewModel
import xyz.tberghuis.noteboat.vm.TrashScreenVm

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TrashNoteCard(
  note: Note,
  vm: TrashScreenVm = viewModel()
) {
  val dismissState = rememberSwipeToDismissBoxState()

  LaunchedEffect(dismissState) {
    // dismissState.confirmValueChange was called twice during tests
    // so using snapshotFlow first() to be safe
    snapshotFlow { dismissState.currentValue }
      .filter {
        it == SwipeToDismissBoxValue.EndToStart
      }
      .first()
    logd("real delete")
    vm.deleteNote(note)
  }

  SwipeToDismissBox(
    state = dismissState,
    backgroundContent = { },
    modifier = Modifier,
    enableDismissFromStartToEnd = false,
    enableDismissFromEndToStart = true,
  ) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 5.dp)
        .combinedClickable(
          onClick = { },
          onLongClick = {
            // todo show restore dialog
            logd("long click")
            // for now just restore
            vm.restoreNote(note)
          },
        ),
      elevation = 10.dp
    ) {
      Column(
        modifier = Modifier.padding(10.dp)
      ) {
        Text(note.noteText)
      }
    }
  }
}