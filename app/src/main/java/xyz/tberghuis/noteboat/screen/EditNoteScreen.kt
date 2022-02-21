package xyz.tberghuis.noteboat.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
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

  Scaffold(
    scaffoldState = scaffoldState,
    topBar = { EditNoteTopBar(showDeleteDialog = showDeleteDialog, onComplete = onComplete) },
    content = {
      NoteContent(
        viewModel.transcribingStateFlow,
        viewModel.noteTextFieldValueState,
        viewModel::updateNote
      )
    },
    floatingActionButtonPosition = FabPosition.End,
    floatingActionButton = {
      TranscribeFloatingActionButton(viewModel.transcribingStateFlow)
    },
  )

  OnLifecycleEvent { owner, event ->
    // do stuff on event
    when (event) {
      Lifecycle.Event.ON_PAUSE -> {
        scope.launch {
          viewModel.transcribingStateFlow.emit(TranscribingState.NOT_TRANSCRIBING)
        }
      }
      else -> { /* other stuff */
      }
    }
  }
}

@Composable
fun EditNoteTopBar(
  onComplete: () -> Unit,
  showDeleteDialog: MutableState<Boolean>,
) {
  TopAppBar(
    modifier = Modifier.statusBarsPadding(),
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

@Composable
fun EditNoteContent(
  viewModel: EditNoteViewModel = hiltViewModel(),
) {
  // todo in future i should wait till noteTextFieldValueState initialized

  Column {
    TextField(
      value = viewModel.noteTextFieldValueState.value,
      onValueChange = {
        viewModel.updateNote(it.text)
        viewModel.noteTextFieldValueState.value = it
      },
      modifier = Modifier
        .navigationBarsWithImePadding()
        .fillMaxSize()
    )
  }
}