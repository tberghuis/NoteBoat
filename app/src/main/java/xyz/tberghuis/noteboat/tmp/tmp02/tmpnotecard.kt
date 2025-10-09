package xyz.tberghuis.noteboat.tmp.tmp02

import android.annotation.SuppressLint
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.PushPin
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import xyz.tberghuis.noteboat.data.Note
import kotlin.math.roundToInt
import xyz.tberghuis.noteboat.LocalNavController

@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun TmpNoteCard(
  note: Note,
  noteNumber: Int?,
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
        navController.navigate("edit-note/${note.noteId}")
      },
//    elevation = 10.dp
  ) {

    Row(
      modifier = Modifier
        .padding(10.dp)
    ) {
      Column(
        modifier = Modifier
          .weight(1f)
      ) {
        if (note.pinned) {
          Icon(
            Icons.TwoTone.PushPin, "pinned",
            modifier = Modifier.offset(x = (-8).dp, y = (-5).dp),
          )
        }
        Text(note.noteText)
      }
      // note number
      if (noteNumber != null) {
        Text(
          noteNumber.toString(),
          modifier = Modifier,
          color = Color.Red,
//    fontSize: TextUnit = TextUnit.Unspecified,
//    fontStyle: FontStyle? = null,
//    fontWeight: FontWeight? = null,
//    fontFamily: FontFamily? = null,
        )
      }
    }
  }
}