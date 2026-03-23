package xyz.tberghuis.noteboat

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
  }
}