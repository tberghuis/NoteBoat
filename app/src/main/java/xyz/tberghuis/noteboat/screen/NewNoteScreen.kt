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
import xyz.tberghuis.noteboat.vm.NewNoteViewModel

@Composable
fun NewNoteScreen(
  navController: NavHostController,
  viewModel: NewNoteViewModel = hiltViewModel(),
) {
  val scaffoldState = rememberScaffoldState()

  // is there a simple way to be loading until draft is loaded from db? meh
  val newNoteDraftState = viewModel.newNoteDraft.collectAsState(initial = "")

//  if (newNoteDraftState.value == null) {
//    return
//  }

  val onComplete: () -> Unit = {
    if (newNoteDraftState.value.trim().isEmpty()) {
      viewModel.updateNewNoteDraft("")
    } else {
      // theoretically possible to overwrite db if press back before data loads
      viewModel.saveNewNote(newNoteDraftState.value)
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
    content = { NewNoteContent(newNoteDraft = newNoteDraftState.value) },
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
  newNoteDraft: String
) {
  val focusRequester = remember { FocusRequester() }

  Column {
    TextField(
      value = newNoteDraft,
      onValueChange = { viewModel.updateNewNoteDraft(it) },
//      label = { Text("new note...") },
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
