package xyz.tberghuis.noteboat.tmp

import android.annotation.SuppressLint
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.math.roundToInt

// todo like this
//class NoteHolder(val note: String) {
//  val isRevealed = false
//}
//
//val noteList = mutableStateListOf<NoteHolder>(
//  NoteHolder("item 1"),
//  NoteHolder("item 2"),
//  NoteHolder("item 3")
//)




val someStringArray = arrayOf("item 1", "item 2", "item 3")

// i could use a set or a hashmap
val revealedStringsList = MutableStateFlow(listOf<Int>())


// on swipe card left (reveal actions)
fun onItemExpanded(cardId: Int) {
  if (revealedStringsList.value.contains(cardId)) return
  revealedStringsList.value = revealedStringsList.value.toMutableList().also { list ->
    list.add(cardId)
  }
}

// on swipe card right (hide actions)
fun onItemCollapsed(cardId: Int) {
  if (!revealedStringsList.value.contains(cardId)) return
  revealedStringsList.value = revealedStringsList.value.toMutableList().also { list ->
    list.remove(cardId)
  }
}


@Composable
fun SwipeDemo() {

  val revealedCardIds = revealedStringsList.collectAsState()

  Scaffold { _ ->


    LazyColumn(Modifier.statusBarsPadding()) {
      itemsIndexed(someStringArray, key = { i, _ -> i }) { i, _ ->

        Box {
          ActionsRow()
          // should i add isRevealed to vm.noteHolder
          // shouldn't really need it i think
          MyCard(i, isRevealed = revealedCardIds.value.contains(i))
        }


      }

    }

  }

}


@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun MyCard(i: Int, isRevealed: Boolean) {

  val transitionState = remember {
    MutableTransitionState(isRevealed).apply {
      targetState = !isRevealed
    }
  }
  val transition = updateTransition(transitionState, "cardTransition")

  val offsetTransition by transition.animateFloat(
    label = "cardOffsetTransition",
    transitionSpec = { tween(durationMillis = 500) },
    targetValueByState = { if (isRevealed) 200f else 0f },

    )


  Card(
    Modifier
      .fillMaxWidth()
      .padding(10.dp)
      .height(50.dp)
      .offset { -IntOffset(offsetTransition.roundToInt(), 0) }
      .pointerInput(Unit) {
        detectHorizontalDragGestures { _, dragAmount ->
          when {
            dragAmount < -10 -> onItemExpanded(i)
            dragAmount > 10 -> onItemCollapsed(i)
          }
        }
      },

    ) {
    Box(contentAlignment = Alignment.Center) {
      Text(someStringArray[i], Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
    }

  }
}


@Preview
@Composable
fun SwipeDemoPreview() {
  SwipeDemo()
}

@Composable
fun ActionsRow() {
  Card(
    Modifier
      .fillMaxWidth()
      .padding(10.dp)
      .height(50.dp),
  ) {
    Box(contentAlignment = Alignment.CenterEnd) {
      Text("actions row")
    }
  }
}
