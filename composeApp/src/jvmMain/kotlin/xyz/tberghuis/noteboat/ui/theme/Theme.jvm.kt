package xyz.tberghuis.noteboat.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
actual fun NoteBoatTheme(
  darkTheme: Boolean,
  dynamicColor: Boolean,
  content: @Composable (() -> Unit)
) {
  // todo copy theme from a fresh KMP project

  MaterialTheme {
    content()
  }

}