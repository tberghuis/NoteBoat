package xyz.tberghuis.noteboat.controller

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue

fun appendAtCursor(tfv: TextFieldValue, result: String): TextFieldValue {
  if (result.isNotEmpty()) {
    val selectionStart = tfv.selection.start
    val text = "${
      tfv.text.substring(
        0,
        selectionStart
      )
    } $result ${tfv.text.substring(selectionStart)}"
    val newCursorPos = selectionStart + result.length + 2
    val textRange = TextRange(newCursorPos, newCursorPos)
    // vm.textFieldValue = vm.textFieldValue.copy(text, textRange)
    return TextFieldValue(text, textRange)
  }
  return tfv
}
