package xyz.tberghuis.noteboat.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import xyz.tberghuis.noteboat.vm.HomeViewModel
import xyz.tberghuis.noteboat.data.Note

@Composable
fun ActionsCard(
  note: Note,
  viewModel: HomeViewModel = viewModel()
) {
  Box(
    Modifier
      .fillMaxWidth(),
    contentAlignment = Alignment.CenterEnd
  ) {
    Row {
      IconButton(onClick = {
        viewModel.togglePinned(note)
      }) {
        Icon(
          if (note.pinned) Icons.Filled.PushPin else Icons.Outlined.PushPin,
          if (note.pinned) "unpin" else "pin",
        )
      }

      IconButton(onClick = {
        // delete that note no warning
        viewModel.trashNote(note)
      }) {
        Icon(Icons.Filled.Delete, "delete")
      }
    }
  }
}