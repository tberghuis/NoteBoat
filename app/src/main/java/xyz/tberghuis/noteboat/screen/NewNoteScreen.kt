package xyz.tberghuis.noteboat.screen

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.launch
import xyz.tberghuis.noteboat.controller.SpeechController
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

  Scaffold(
    scaffoldState = scaffoldState,
    topBar = { NewNoteTopBar(onComplete = onComplete, onCancel = onCancel) },
    content = { NewNoteContent() },
    floatingActionButtonPosition = FabPosition.End,
    floatingActionButton = {
      TranscribeFloatingActionButton()
    },
  )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TranscribeFloatingActionButton() {
  val viewModel: NewNoteViewModel = hiltViewModel()
  val keyboardController = LocalSoftwareKeyboardController.current
  val transcribingState = viewModel.transcribingStateFlow.collectAsState()
  val context = LocalContext.current

  val launcher = rememberLauncherForActivityResult(
    ActivityResultContracts.RequestPermission()
  ) {}

  var fabOnClick: () -> Unit = {
    Log.d("xxx", "fabOnClick NOT_TRANSCRIBING")
    when (PackageManager.PERMISSION_GRANTED) {
      ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.RECORD_AUDIO
      ) -> {
        keyboardController?.hide()
        viewModel.transcribingStateFlow.value = TranscribingState.TRANSCRIBING
      }
      else -> {
        launcher.launch(Manifest.permission.RECORD_AUDIO)
      }
    }
  }

  var fabIcon = @Composable { Icon(Icons.Filled.Mic, "speech input") }
  if (transcribingState.value == TranscribingState.TRANSCRIBING) {

    fabOnClick = {
      viewModel.transcribingStateFlow.value = TranscribingState.NOT_TRANSCRIBING
    }
    fabIcon = @Composable { Icon(Icons.Filled.MicOff, "stop speech input") }
  }

  FloatingActionButton(
    modifier = Modifier.navigationBarsWithImePadding(),
    onClick = fabOnClick
  ) {
    fabIcon()
  }
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

@Composable
fun NewNoteContent(
  // todo pass in ITextFieldViewModel
  viewModel: NewNoteViewModel = hiltViewModel(),
) {
  val focusRequester = remember { FocusRequester() }
  val transcribing = viewModel.transcribingStateFlow.collectAsState()
  val onValueChange by derivedStateOf<(TextFieldValue) -> Unit> {
    // replace with when when i learn more kotlin
    if (transcribing.value == TranscribingState.NOT_TRANSCRIBING) {
      return@derivedStateOf {
        viewModel.updateNewNoteDraft(it.text)
        viewModel.noteTextFieldValueState.value = it
      }
    }
    { }
  }

  Column {
    Box {
      TextField(
        value = viewModel.noteTextFieldValueState.value,
        // todo call ITextFieldViewModel.onValueChange
        onValueChange = onValueChange,
        modifier = Modifier
          .focusRequester(focusRequester)
          .navigationBarsWithImePadding()
          .fillMaxSize()
      )
      // don't allow clicks on textfield
      if (transcribing.value == TranscribingState.TRANSCRIBING) {
        Box(
          modifier = Modifier
            .matchParentSize()
            .alpha(0f)
            .clickable(onClick = { }),
        )
      }
    }
  }

  LaunchedEffect(Unit) {
    // todo place cursor at last position
    focusRequester.requestFocus()
  }
}