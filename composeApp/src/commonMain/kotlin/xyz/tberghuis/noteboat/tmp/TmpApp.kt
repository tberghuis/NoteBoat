package xyz.tberghuis.noteboat.tmp


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import xyz.tberghuis.noteboat.ui.theme.NoteBoatTheme

@Composable
@Preview
fun TmpApp() {
  NoteBoatTheme {
    Surface(modifier = Modifier.fillMaxSize()) {
      Column {
        LinkDisplay()
      }
    }
  }
}