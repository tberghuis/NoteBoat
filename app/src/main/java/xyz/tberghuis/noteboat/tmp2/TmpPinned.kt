package xyz.tberghuis.noteboat.tmp2

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TmpPinned() {
  Column {
    TmpNoteCard()
    TmpNoteCard()
  }
}

@Composable
fun TmpNoteCard() {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 5.dp), elevation = 10.dp
  ) {
    Column(
      modifier = Modifier.padding(10.dp)
    ) {
      Text("hello note")
    }
  }

}