package xyz.tberghuis.noteboat.tmp2

import androidx.activity.compose.BackHandler
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import xyz.tberghuis.noteboat.composable.OnPauseLifecycleEvent
import xyz.tberghuis.noteboat.screen.NewNoteTopBar
import xyz.tberghuis.noteboat.vm.NewNoteViewModel
import xyz.tberghuis.noteboat.vm.TranscribingState

@OptIn(
  ExperimentalComposeUiApi::class,
)
@Composable
fun NewNoteScreen(
  navController: NavHostController,
  viewModel: NewNoteViewModel = hiltViewModel(),
) {
  val scaffoldState = rememberScaffoldState()
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

  BackHandler {
    onComplete()
  }

  OnPauseLifecycleEvent(viewModel.transcribingStateFlow)

  Scaffold(
    scaffoldState = scaffoldState,
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

  val keyboardController = LocalSoftwareKeyboardController.current
  LaunchedEffect(true) {
    viewModel.transcribingStateFlow.collect {
      when (it) {
        TranscribingState.TRANSCRIBING -> {
          keyboardController?.hide()
        }

        else -> {
          // todo
        }
      }
    }
  }
}