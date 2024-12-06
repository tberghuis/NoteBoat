package xyz.tberghuis.noteboat.screen

import android.annotation.SuppressLint
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.twotone.PushPin
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import xyz.tberghuis.noteboat.vm.HomeViewModel
import xyz.tberghuis.noteboat.data.Note
import kotlin.math.roundToInt
import xyz.tberghuis.noteboat.LocalNavController
import xyz.tberghuis.noteboat.R
import xyz.tberghuis.noteboat.composable.ActionsCard

@Composable
fun HomeScreen() {
  val navController = LocalNavController.current
  val scaffoldState = rememberScaffoldState()
  Scaffold(
    scaffoldState = scaffoldState,
    topBar = { HomeTopBar() },
    floatingActionButtonPosition = FabPosition.End,
    floatingActionButton = {
      FloatingActionButton(
        modifier = Modifier.navigationBarsPadding(),
        onClick = {
          navController.navigate("new-note")
        }) {
        Icon(Icons.Filled.Add, "new note")
      }
    },
    content = { contentPadding ->
      Box(
        Modifier
          .padding(contentPadding)
          .navigationBarsPadding()
      ) {
        HomeContent()
      }
    },
  )
}

@Composable
fun HomeTopBar() {
  val navController = LocalNavController.current
  TopAppBar(
    modifier = Modifier
      .statusBarsPadding(),
//      .navigationBarsPadding(),
    title = { Text("Note Boat") },
    actions = {
      IconButton(onClick = {
        navController.navigate("trash")
      }) {
        Icon(Icons.Filled.Delete, stringResource(R.string.trash))
      }
      IconButton(onClick = {
        navController.navigate("settings")
      }) {
        Icon(Icons.Filled.Settings, stringResource(R.string.settings))
      }
    }
  )
}

@Composable
fun HomeContent() {
  val viewModel: HomeViewModel = viewModel()
  val allNotes = viewModel.allNotes.collectAsState(listOf())
  val offsetNotes = viewModel.offsetNotes.collectAsState(setOf())
  LazyColumn(
    contentPadding = PaddingValues(10.dp)
  ) {
    // when key = note_id there was a bug, could not swipe to reveal after toggling note.pinned
    items(allNotes.value,
      key = { it.hashCode() }
    ) { note ->
      Box(
        Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
      ) {
        ActionsCard(note)
        NoteCard(
          note,
          offsetNotes.value.contains(note),
          viewModel::onRevealActions,
          viewModel::onHideActions
        )
      }
    }
    item {
      // clear FAB
      Spacer(Modifier.height(70.dp))
    }
  }
}

// doitwrong
@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun NoteCard(
  note: Note,
  isOffset: Boolean,
  onRevealActions: (note: Note) -> Unit,
  onHideActions: (note: Note) -> Unit,
) {
  val navController = LocalNavController.current
  val transitionState = remember {
    MutableTransitionState(isOffset).apply {
      targetState = !isOffset
    }
  }
  val transition = rememberTransition(transitionState, "cardTransition")
  val offsetTransition by transition.animateFloat(
    label = "cardOffsetTransition",
    transitionSpec = { tween(durationMillis = 500) },
    targetValueByState = { if (isOffset) -350f else 0f },
  )

  Card(
    modifier = Modifier
      .fillMaxWidth()
      // in future use Spacer instead of unfunctional padding
      .padding(vertical = 5.dp)
      // play around
      .offset { IntOffset(offsetTransition.roundToInt(), 0) }
      .pointerInput(Unit) {
        detectHorizontalDragGestures { _, dragAmount ->
          when {
            dragAmount > 20 -> onHideActions(note)
            dragAmount < -20 -> onRevealActions(note)
          }
        }
      }
      .clickable {
        // todo nav edit note
        navController.navigate("edit-note/${note.noteId}")
      },
    elevation = 10.dp
  ) {
    Column(
      modifier = Modifier.padding(10.dp)
    ) {

      if (note.pinned) {
        Icon(
          Icons.TwoTone.PushPin, "pinned",
          modifier = Modifier.offset(x = (-8).dp, y = (-5).dp),
        )
      }
      Text(note.noteText)
    }
  }
}