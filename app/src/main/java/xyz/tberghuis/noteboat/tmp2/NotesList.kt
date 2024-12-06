package xyz.tberghuis.noteboat.tmp2

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun NotesList() {
  val viewModel: TrashScreenVm = viewModel()
  val trashNotes = viewModel.trashNotes.collectAsState(listOf())
  LazyColumn(
    contentPadding = PaddingValues(10.dp)
  ) {
    items(trashNotes.value,
      key = { it.hashCode() }
    ) { note ->


      val dismissState = rememberSwipeToDismissBoxState()
      SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
          val color by
          animateColorAsState(
            when (dismissState.targetValue) {
              SwipeToDismissBoxValue.Settled -> Color.LightGray
              SwipeToDismissBoxValue.StartToEnd -> Color.Green
              SwipeToDismissBoxValue.EndToStart -> Color.Red
            }
          )
          Box(
            Modifier
              .fillMaxSize()
              .background(color)
          )
        }
      ) {

//        TmpNoteCard(note)

        OutlinedCard(shape = RectangleShape) {
          ListItem(
            headlineContent = { Text("Cupcake") },
            supportingContent = { Text("Swipe me left or right!") }
          )
        }


      }


    }
  }
}