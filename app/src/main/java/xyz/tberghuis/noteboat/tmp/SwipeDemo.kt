package xyz.tberghuis.noteboat.tmp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.flow.MutableStateFlow


val someStringArray = arrayOf("item 1", "item 2", "item 3")

val revealedStringsList = MutableStateFlow(listOf<Int>())

fun onItemExpanded(cardId: Int) {
  if (revealedStringsList.value.contains(cardId)) return
  revealedStringsList.value = revealedStringsList.value.toMutableList().also { list ->
    list.add(cardId)
  }
}

fun onItemCollapsed(cardId: Int) {
  if (!revealedStringsList.value.contains(cardId)) return
  revealedStringsList.value = revealedStringsList.value.toMutableList().also { list ->
    list.remove(cardId)
  }
}


@Composable
fun SwipeDemo() {

  Scaffold { _ ->


    LazyColumn(Modifier.statusBarsPadding()) {
      itemsIndexed(someStringArray, key = { i, _ -> i }) { i, _ ->

        Box {
          ActionsRow()
          MyCard(i)
        }


      }

    }

  }

}

@Composable
fun MyCard(i: Int) {
  Card(
    Modifier
      .fillMaxWidth()
      .padding(10.dp)
      .height(50.dp),

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
