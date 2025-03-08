package xyz.tberghuis.noteboat.tmp.tmp00

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import xyz.tberghuis.noteboat.ui.theme.NoteBoatTheme

class TmpActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      NoteBoatTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
        ) {
          TmpScreen()
        }
      }
    }
  }
}