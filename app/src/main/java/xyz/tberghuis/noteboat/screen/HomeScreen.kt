package xyz.tberghuis.noteboat.screen

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import xyz.tberghuis.noteboat.vm.HomeViewModel
import com.google.accompanist.insets.ui.Scaffold
import xyz.tberghuis.noteboat.data.Note

@Composable
fun HomeScreen(
//  viewModel: HomeViewModel = hiltViewModel(),
  navController: NavHostController
) {
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
        HomeContent(navController = navController)
      }
    },
  )
}

@Composable
fun HomeTopBar() {
  TopAppBar(
    modifier = Modifier
      .statusBarsPadding()
      .navigationBarsPadding(bottom = false),
    title = { Text("Note Boat") }
  )
}

@Composable
fun HomeContent(
  viewModel: HomeViewModel = hiltViewModel(),
  navController: NavHostController,
//  paddingValues: PaddingValues
) {
  val allNotes = viewModel.allNotes.collectAsState(listOf())
  LazyColumn(
    contentPadding = PaddingValues(10.dp)
  ) {
    items(items = allNotes.value) { note ->

      Box(
        Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
      ) {
        ActionsCard(note)
        NoteCard(navController, note)
      }

    }
  }
}

@Composable
fun ActionsCard(note: Note, viewModel: HomeViewModel = hiltViewModel()) {


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


@Composable
fun NoteCard(
  navController: NavHostController,
  note: Note
) {

//  val transitionState = remember {
//    MutableTransitionState(isRevealed).apply {
//      targetState = !isRevealed
//    }
//  }


  Card(
    modifier = Modifier
      .fillMaxWidth()
      // in future use Spacer instead of unfunctional padding
      .padding(vertical = 5.dp)
      // play around
      .offset { IntOffset(-200, 0) }
      .clickable {
        // todo nav edit note
        navController.navigate("edit-note/${note.noteId}")
      },
    elevation = 10.dp
  ) {
    Column(
      modifier = Modifier.padding(10.dp)
    ) {
      Text(note.noteText)
    }
  }

}
