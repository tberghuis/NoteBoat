package xyz.tberghuis.noteboat.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.coroutines.flow.StateFlow
import xyz.tberghuis.noteboat.vm.TranscribingState

@Composable
fun NoteContent(
  paddingValues: PaddingValues,
  transcribingStateFlow: StateFlow<TranscribingState>,
  noteTextFieldValueState: MutableState<TextFieldValue>,
  updateDb: (String) -> Unit
) {
  val focusRequester = remember { FocusRequester() }
  val transcribing = transcribingStateFlow.collectAsState()
  val onValueChange by remember {
    derivedStateOf<(TextFieldValue) -> Unit> {
      // replace with when when i learn more kotlin
      if (transcribing.value == TranscribingState.NOT_TRANSCRIBING) {
        return@derivedStateOf {
          // this is bad, probably better to updateDb from
          // launchedEffect snapshotFlow
          updateDb(it.text)
          noteTextFieldValueState.value = it
        }
      }
      { }
    }
  }

  Column(Modifier.padding(paddingValues)) {
    Box {
      // todo in future i should wait till noteTextFieldValueState initialized
      TextField(
        value = noteTextFieldValueState.value,
        // todo call ITextFieldViewModel.onValueChange
        onValueChange = onValueChange,
        modifier = Modifier
          .focusRequester(focusRequester)
          .fillMaxSize()
      )
      // don't allow clicks on TextField
      if (transcribing.value == TranscribingState.TRANSCRIBING) {
        Box(
          modifier = Modifier
            .matchParentSize()
            .alpha(0f)
            .clickable(onClick = { }),
        )
      }
    }
  }

  LaunchedEffect(Unit) {
    // todo place cursor at last position
    focusRequester.requestFocus()
  }
}