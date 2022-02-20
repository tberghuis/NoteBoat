package xyz.tberghuis.noteboat.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.insets.imePadding
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.flow.collect
import xyz.tberghuis.noteboat.vm.NewNoteViewModel

@Composable
fun NewNoteScreen(
  navController: NavHostController,
  viewModel: NewNoteViewModel = hiltViewModel(),
) {
  val scaffoldState = rememberScaffoldState()

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
