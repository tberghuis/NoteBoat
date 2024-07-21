package xyz.tberghuis.noteboat.screen

import android.annotation.SuppressLint
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.navigation.NavHostController
import xyz.tberghuis.noteboat.vm.HomeViewModel
import xyz.tberghuis.noteboat.data.Note
import kotlin.math.roundToInt
import xyz.tberghuis.noteboat.R
import xyz.tberghuis.noteboat.tmp2.TmpActionsCard

@Composable
fun HomeScreen(
  navController: NavHostController
) {
  val scaffoldState = rememberScaffoldState()
  Scaffold(
    scaffoldState = scaffoldState,
    topBar = { HomeTopBar(navController) },
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
        HomeContent(navController = navController)
      }
    },
  )
}

@Composable
fun HomeTopBar(navController: NavHostController) {
  TopAppBar(
    modifier = Modifier
      .statusBarsPadding(),
//      .navigationBarsPadding(),
    title = { Text("Note Boat") },
    actions = {
      IconButton(onClick = {
        navController.navigate("settings")
      }) {
        Icon(Icons.Filled.Settings, stringResource(R.string.settings))
      }

    }
  )


}

@Composable
fun HomeContent(
  navController: NavHostController,
) {
  val viewModel: HomeViewModel = viewModel()

  val allNotes = viewModel.allNotes.collectAsState(listOf())
  val offsetNotes = viewModel.offsetNotes.collectAsState(setOf())

  LazyColumn(
    contentPadding = PaddingValues(10.dp)
  ) {

    itemsIndexed(allNotes.value, key = { _, note -> note.noteId })
    { _, note ->
      Box(
        Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
      ) {
//        ActionsCard(note)
        
        TmpActionsCard(note)


        NoteCard(
          navController,
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

@Composable
fun ActionsCard(
  note: Note,
  viewModel: HomeViewModel = viewModel()
) {


  Box(
    Modifier
      .fillMaxWidth(),
    contentAlignment = Alignment.CenterEnd
  ) {
    IconButton(onClick = {
      // delete that note no warning
      viewModel.deleteNote(note)
    }) {
      Icon(Icons.Filled.Delete, "delete")
    }
  }
}

//@Composable
//fun DraggableNoteCard(
//  navController: NavHostController,
//  note: Note
//){
//
//}

// read docs to understand if i am doing things wrong
// doitwrong
@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun NoteCard(
  navController: NavHostController,
  note: Note,
  isOffset: Boolean,
  onRevealActions: (note: Note) -> Unit,
  onHideActions: (note: Note) -> Unit,
) {

  val transitionState = remember {
    MutableTransitionState(isOffset).apply {
      targetState = !isOffset
    }
  }
  val transition = updateTransition(transitionState, "cardTransition")
  val offsetTransition by transition.animateFloat(
    label = "cardOffsetTransition",
    transitionSpec = { tween(durationMillis = 500) },
    targetValueByState = { if (isOffset) -200f else 0f },
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
