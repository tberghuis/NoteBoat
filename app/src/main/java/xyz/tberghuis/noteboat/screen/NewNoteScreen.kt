package xyz.tberghuis.noteboat.screen

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
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
  val context = LocalContext.current
  val scope = rememberCoroutineScope()

  val speechController = remember {
    SpeechController(context, viewModel, scope)
  }

  val onComplete: () -> Unit = {
    if (viewModel.noteTextFieldValue.text.trim().isEmpty()) {
      viewModel.updateNewNoteDraft("")
    } else {
      // theoretically possible to overwrite db if press back before data loads
      viewModel.saveNewNote(viewModel.noteTextFieldValue.text)
    }
    navController.navigateUp()
  }

  val onCancel: () -> Unit = {
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
  viewModel: NewNoteViewModel = hiltViewModel(),
) {
  val focusRequester = remember { FocusRequester() }

  Column {
    TextField(
      value = viewModel.noteTextFieldValue,
      onValueChange = {
        viewModel.updateNewNoteDraft(it.text)
        viewModel.noteTextFieldValue = it
      },
      modifier = Modifier
        .focusRequester(focusRequester)
        .navigationBarsWithImePadding()
        .fillMaxSize()
    )
  }

  LaunchedEffect(Unit) {
    focusRequester.requestFocus()
  }
}
