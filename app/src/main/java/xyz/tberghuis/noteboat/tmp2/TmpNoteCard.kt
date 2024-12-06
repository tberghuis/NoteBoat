package xyz.tberghuis.noteboat.tmp2

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.ui.unit.dp
import xyz.tberghuis.noteboat.data.Note
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import xyz.tberghuis.noteboat.utils.logd

@Composable
fun TmpNoteCard(
  note: Note,
) {
  val dismissState = rememberSwipeToDismissBoxState(
//    confirmValueChange = {
//      if (it == SwipeToDismissBoxValue.EndToStart) {
//        logd("swipe to delete note")
//      }
//      true
//    },
  )

  LaunchedEffect(dismissState) {
    snapshotFlow { dismissState.currentValue }
      .filter {
        it == SwipeToDismissBoxValue.EndToStart
      }
      .first()
    // todo vm.realDelete
    logd("real delete")
  }

  SwipeToDismissBox(
    state = dismissState,
    backgroundContent = {
      val color by
      animateColorAsState(
        when (dismissState.targetValue) {
          SwipeToDismissBoxValue.Settled -> Color.LightGray
          SwipeToDismissBoxValue.StartToEnd -> Color.Green
          SwipeToDismissBoxValue.EndToStart -> Color.Red
        }, label = "ColorAnimation"
      )
      Box(
        Modifier
          .fillMaxSize()
          .background(color)
      )
    }
  ) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 5.dp)
        .clickable {
          // todo long press "restore note" dialog
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
}