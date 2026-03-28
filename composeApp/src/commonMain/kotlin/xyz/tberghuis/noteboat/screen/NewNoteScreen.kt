package xyz.tberghuis.noteboat.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import kotlinx.coroutines.launch
import xyz.tberghuis.noteboat.composable.OnPauseLifecycleEvent
import xyz.tberghuis.noteboat.vm.NewNoteViewModel
import xyz.tberghuis.noteboat.composable.NoteBottomAppBar
import xyz.tberghuis.noteboat.composable.NoteContent
import org.koin.compose.viewmodel.koinViewModel
import xyz.tberghuis.noteboat.LocalNavController
import xyz.tberghuis.noteboat.controller.TranscribingState

@Composable
fun NewNoteScreen(
  viewModel: NewNoteViewModel = koinViewModel(),
) {
  val navController = LocalNavController.current
  val scope = rememberCoroutineScope()
  val onComplete: () -> Unit = {
    if (viewModel.noteTextFieldValueState.value.text.trim().isEmpty()) {
      viewModel.updateNewNoteDraft("")
    } else {
      // theoretically possible to overwrite db if press back before data loads
      viewModel.saveNewNote(viewModel.noteTextFieldValueState.value.text)
    }
    navController.navigateUp()
  }

  val onCancel: () -> Unit = {
    scope.launch {
      viewModel.transcribingStateFlow.emit(TranscribingState.NOT_TRANSCRIBING)
    }
    viewModel.noteTextFieldValueState.value = TextFieldValue()
    viewModel.updateNewNoteDraft("")
    navController.navigateUp()
  }

  NavigationBackHandler(
    state = rememberNavigationEventState(NavigationEventInfo.None),
  ) {
    onComplete()
  }

  OnPauseLifecycleEvent(viewModel.transcribingStateFlow)

  Scaffold(
    topBar = { NewNoteTopBar(onComplete = onComplete, onCancel = onCancel) },
    content = { paddingValues ->
      NoteContent(
        paddingValues,
        viewModel.transcribingStateFlow,
        viewModel.noteTextFieldValueState,
        viewModel::updateNewNoteDraft
      )
    },
    bottomBar = {
      NoteBottomAppBar(viewModel.transcribingStateFlow, onComplete)
    }
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewNoteTopBar(
  onComplete: () -> Unit,
  onCancel: () -> Unit
) {
  TopAppBar(
    modifier = Modifier,
    title = { Text("New Note") },
    navigationIcon = {
      IconButton(onClick = {
        onComplete()
      }) {
        Icon(Icons.AutoMirrored.Filled.ArrowBack, "complete")
      }
    },
    actions = {
      IconButton(onClick = { onCancel() }) {
        Icon(Icons.Filled.Cancel, "cancel")
      }
    }
  )
}