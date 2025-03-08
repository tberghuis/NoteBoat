package xyz.tberghuis.noteboat.tmp.tmp00

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import xyz.tberghuis.noteboat.ui.theme.NoteBoatTheme

@Composable
fun TmpScreen() {
  Column {
    Text("hello tmp screen")
  }
}

@Preview
@Composable
fun TmpScreenPreview() {
  NoteBoatTheme {
    Surface(
      modifier = Modifier.fillMaxSize(),
    ) {
      TmpScreen()
    }
  }
}