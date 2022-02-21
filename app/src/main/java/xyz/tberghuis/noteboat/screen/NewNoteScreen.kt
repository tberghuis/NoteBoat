package xyz.tberghuis.noteboat.screen

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.TextFieldValue
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import xyz.tberghuis.noteboat.composable.NoteContent
import xyz.tberghuis.noteboat.composable.OnPauseLifecycleEvent
import xyz.tberghuis.noteboat.composable.TranscribeFloatingActionButton
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
    content = {
      NoteContent(
        viewModel.transcribingStateFlow,
        viewModel.noteTextFieldValueState,
        viewModel::updateNewNoteDraft
      )
    },
    floatingActionButtonPosition = FabPosition.End,
    floatingActionButton = {
      TranscribeFloatingActionButton(viewModel.transcribingStateFlow)
    },
  )
}


@Composable
fun NewNoteTopBar(
  onComplete: () -> Unit,
  onCancel: () -> Unit
) {
  TopAppBar(
    modifier = Modifier.statusBarsPadding(),
    title = { Text("New Note") },
    navigationIcon = {
      IconButton(onClick = {
        onComplete()
      }) {
        Icon(Icons.Filled.ArrowBack, "complete")
      }
    },
    actions = {
      IconButton(onClick = { onCancel() }) {
        Icon(Icons.Filled.Cancel, "cancel")
      }
    }
  )
}





