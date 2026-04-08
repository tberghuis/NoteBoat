package xyz.tberghuis.noteboat

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavKey
import xyz.tberghuis.noteboat.nav.NoteBoatNavDisplay
import xyz.tberghuis.noteboat.nav.RouteHome
import xyz.tberghuis.noteboat.ui.theme.NoteBoatTheme

@Composable
@Preview
fun App() {
//  MaterialTheme {
//    TmpScreen()
//  }

  val initialBackStack = remember { mutableStateListOf<NavKey>(RouteHome) }

  NoteBoatTheme {
    Surface(modifier = Modifier.fillMaxSize()) {
      NoteBoatNavDisplay(initialBackStack)
    }
//        TmpScreen()
  }

}