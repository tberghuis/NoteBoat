package xyz.tberghuis.noteboat.composable

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.AlertDialog
import org.koin.compose.viewmodel.koinViewModel
import xyz.tberghuis.noteboat.vm.SettingsViewModel

@Composable
fun DeleteAllNotesButton(
  vm: SettingsViewModel = koinViewModel()
) {
  Button(onClick = {
    vm.confirmDeleteAllNotesDialog = true
  }) {
    Text("Delete All Notes")
  }
  if (vm.confirmDeleteAllNotesDialog) {
    AlertDialog(
      onDismissRequest = { vm.confirmDeleteAllNotesDialog = false },
      confirmButton = {
        Button(onClick = {
          vm.confirmDeleteAllNotesDialog = false
          vm.deleteAllNotes()
        }) {
          Text("OK")
        }
      },
      modifier = Modifier,
      dismissButton = {
        Button(onClick = {
          vm.confirmDeleteAllNotesDialog = false
        }) {
          Text("Cancel")
        }
      },
      text = {
        Text("Confirm delete all notes?")
      },
    )
  }
}
