package xyz.tberghuis.noteboat.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import xyz.tberghuis.noteboat.composable.NoteBottomAppBar
import xyz.tberghuis.noteboat.composable.NoteContent
import xyz.tberghuis.noteboat.composable.OnPauseLifecycleEvent
import xyz.tberghuis.noteboat.vm.EditNoteViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import xyz.tberghuis.noteboat.LocalNavController

@Composable
fun EditNoteScreen(
  viewModel: EditNoteViewModel = viewModel(),
) {
  val navController = LocalNavController.current
//  val scaffoldState = rememberScaffoldState()
  val onComplete: () -> Unit = {
    viewModel.updateNote(viewModel.noteTextFieldValueState.value.text)
    navController.navigateUp()
  }
  BackHandler {
    onComplete()
  }

  val showDeleteDialog = remember { mutableStateOf(false) }

  if (showDeleteDialog.value) {
    AlertDialog(
      text = {
        Text("Confirm Delete?")
      },
      confirmButton = {
        Button(onClick = {
          showDeleteDialog.value = false
          viewModel.trashNote()
          onComplete()
        }) {
          Text("OK")
        }
      },
      dismissButton = {
        Button(onClick = {
          showDeleteDialog.value = false
        }) {
          Text("Cancel")
        }
      },
      onDismissRequest = {
        showDeleteDialog.value = false
      })
  }

  OnPauseLifecycleEvent(viewModel.transcribingStateFlow)

  Scaffold(
//    scaffoldState = scaffoldState,
    topBar = { EditNoteTopBar(showDeleteDialog = showDeleteDialog, onComplete = onComplete) },
    content = { paddingValues ->
      NoteContent(
        paddingValues,
        viewModel.transcribingStateFlow,
        viewModel.noteTextFieldValueState,
        viewModel::updateNote
      )
    },
    bottomBar = {
      NoteBottomAppBar(viewModel.transcribingStateFlow, onComplete)
    }
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNoteTopBar(
  onComplete: () -> Unit,
  showDeleteDialog: MutableState<Boolean>,
) {
  TopAppBar(
    modifier = Modifier
      .statusBarsPadding(),

    title = { Text("Edit Note") },
    navigationIcon = {
      IconButton(onClick = {
        onComplete()
      }) {
        Icon(Icons.AutoMirrored.Filled.ArrowBack, "complete")
      }
    },
    actions = {
      IconButton(onClick = {
        showDeleteDialog.value = true
      }) {
        Icon(Icons.Filled.Delete, "delete")
      }
    }
  )
}