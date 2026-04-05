package xyz.tberghuis.noteboat

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import xyz.tberghuis.noteboat.di.initKoin

fun main() = application {
  initKoin()
  Window(
    onCloseRequest = ::exitApplication,
    title = "NoteBoat",
  ) {
    App()
//    Xxx()
  }
}

@Composable
fun Xxx() {
  Column {
    Text("hello world")
  }
}