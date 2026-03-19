package xyz.tberghuis.noteboat.composable

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.MutableStateFlow
import xyz.tberghuis.noteboat.utils.logd
import xyz.tberghuis.noteboat.vm.TranscribingState

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

@Composable
fun PushToTranscribe(
  transcribingStateFlow: MutableStateFlow<TranscribingState>,
) {
  val launcher = rememberLauncherForActivityResult(
    ActivityResultContracts.RequestPermission()
  ) {}
  val context = LocalContext.current
  val interactionSource = remember { MutableInteractionSource() }
  interactionSource.onIsPressedStateChanged(
    onPressBegin = {
      logd("press begin")
      if (ContextCompat.checkSelfPermission(
          context,
          Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
      ) {
        transcribingStateFlow.value = TranscribingState.TRANSCRIBING
      } else {
        launcher.launch(Manifest.permission.RECORD_AUDIO)
      }
    },
    onPressEnd = {
      logd("press end")
      transcribingStateFlow.value = TranscribingState.NOT_TRANSCRIBING
    },
  )

  FloatingActionButton(
    modifier = Modifier
      .padding(end = 20.dp),
    onClick = { },
    interactionSource = interactionSource,
  ) {
    Icon(Icons.Filled.Mic, "push to transcribe")
  }
}