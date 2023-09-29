package xyz.tberghuis.noteboat.tmp2

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.BottomAppBar
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NoteBottomAppBar() {
  BottomAppBar(
    modifier = Modifier
      .navigationBarsPadding()
      .imePadding(),
//      .fillMaxWidth(),
    backgroundColor = MaterialTheme.colors.background,
    contentPadding = PaddingValues(20.dp),
  ) {
    Text("bottom bar")
  }
}