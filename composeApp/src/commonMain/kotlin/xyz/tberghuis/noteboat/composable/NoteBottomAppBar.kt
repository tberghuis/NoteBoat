package xyz.tberghuis.noteboat.composable

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import xyz.tberghuis.noteboat.controller.TranscribingState
import xyz.tberghuis.noteboat.utils.logd

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NoteBottomAppBar(
  transcribingStateFlow: MutableStateFlow<TranscribingState>,
  onComplete: () -> Unit,
) {
  BottomAppBar(
    modifier = Modifier
      .imePadding(),
    contentPadding = PaddingValues(20.dp),
  ) {
    Spacer(Modifier.weight(1f))
    PushToTranscribe(transcribingStateFlow)
    FloatingActionButton(
      onClick = onComplete
    ) {
      Icon(Icons.Filled.Save, contentDescription = "save")
    }
  }
}

