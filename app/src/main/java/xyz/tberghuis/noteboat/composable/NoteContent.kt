package xyz.tberghuis.noteboat.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.TextFieldValue
import com.google.accompanist.insets.navigationBarsWithImePadding
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
  val onValueChange by derivedStateOf<(TextFieldValue) -> Unit> {
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

  Column(Modifier.padding(paddingValues)) {
    Box {
      // todo in future i should wait till noteTextFieldValueState initialized
      TextField(
        value = noteTextFieldValueState.value,
        // todo call ITextFieldViewModel.onValueChange
        onValueChange = onValueChange,
        modifier = Modifier
          .focusRequester(focusRequester)
          .navigationBarsWithImePadding()
          .fillMaxSize()
      )
      // don't allow clicks on textfield
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
