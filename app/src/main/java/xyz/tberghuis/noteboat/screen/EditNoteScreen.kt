package xyz.tberghuis.noteboat.screen

import androidx.activity.compose.BackHandler
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.launch
import xyz.tberghuis.noteboat.composable.NoteContent
import xyz.tberghuis.noteboat.composable.OnPauseLifecycleEvent
import xyz.tberghuis.noteboat.composable.TranscribeFloatingActionButton
import xyz.tberghuis.noteboat.vm.EditNoteViewModel
import xyz.tberghuis.noteboat.vm.TranscribingState

@Composable
fun EditNoteScreen(
  viewModel: EditNoteViewModel = hiltViewModel(),
  navController: NavHostController
) {
  val scaffoldState = rememberScaffoldState()
  val scope = rememberCoroutineScope()
  val onComplete: () -> Unit = {
    scope.launch {
      viewModel.transcribingStateFlow.emit(TranscribingState.NOT_TRANSCRIBING)
      navController.navigateUp()
    }
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
          viewModel.deleteNote()
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
    scaffoldState = scaffoldState,
    topBar = { EditNoteTopBar(showDeleteDialog = showDeleteDialog, onComplete = onComplete) },
    content = { paddingValues ->
      NoteContent(
        paddingValues,
        viewModel.transcribingStateFlow,
        viewModel.noteTextFieldValueState,
        viewModel::updateNote
      )
    },
    floatingActionButtonPosition = FabPosition.End,
    floatingActionButton = {
      TranscribeFloatingActionButton(viewModel.transcribingStateFlow, onComplete)
    },
  )
}

@Composable
fun EditNoteTopBar(
  onComplete: () -> Unit,
  showDeleteDialog: MutableState<Boolean>,
) {
  TopAppBar(
    modifier = Modifier
      .statusBarsPadding()
      .navigationBarsPadding(bottom = false),
    title = { Text("Edit Note") },
    navigationIcon = {
      IconButton(onClick = {
        onComplete()
      }) {
        Icon(Icons.Filled.ArrowBack, "complete")
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