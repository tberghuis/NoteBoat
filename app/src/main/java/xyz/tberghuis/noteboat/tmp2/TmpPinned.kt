package xyz.tberghuis.noteboat.tmp2

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material.icons.rounded.PushPin
import androidx.compose.material.icons.sharp.PushPin
import androidx.compose.material.icons.twotone.PushPin
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

//      Icon(Icons.Filled.PushPin, "complete")
//      Icon(Icons.Sharp.PushPin, "complete")
//      Icon(Icons.Outlined.PushPin, "complete")
      Icon(
        Icons.TwoTone.PushPin, "pinned",
        modifier = Modifier.offset(x = (-8).dp, y = (-5).dp),
      )
//      Icon(Icons.Rounded.PushPin, "complete")

      Text("hello note")
    }
  }

}