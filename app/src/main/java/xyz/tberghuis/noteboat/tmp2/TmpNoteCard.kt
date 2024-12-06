package xyz.tberghuis.noteboat.tmp2

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import xyz.tberghuis.noteboat.LocalNavController
import xyz.tberghuis.noteboat.data.Note

@Composable
fun TmpNoteCard(
  note: Note,
) {
  val navController = LocalNavController.current


  Card(
    modifier = Modifier
      .fillMaxWidth()
      // in future use Spacer instead of unfunctional padding
      .padding(vertical = 5.dp)
      .pointerInput(Unit) {
        detectHorizontalDragGestures { _, dragAmount ->
          when {
//            dragAmount > 20 -> onHideActions(note)
//            dragAmount < -20 -> onRevealActions(note)
          }
        }
      }
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