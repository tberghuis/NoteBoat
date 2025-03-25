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
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.MutableStateFlow
import xyz.tberghuis.noteboat.tmp.tmp03.onIsPressedStateChanged
import xyz.tberghuis.noteboat.utils.logd
import xyz.tberghuis.noteboat.vm.TranscribingState

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NoteBottomAppBar(
  transcribingStateFlow: MutableStateFlow<TranscribingState>,
  onComplete: () -> Unit,
) {
  val keyboardController = LocalSoftwareKeyboardController.current
  val transcribingState = transcribingStateFlow.collectAsState()
  val context = LocalContext.current

  val launcher = rememberLauncherForActivityResult(
    ActivityResultContracts.RequestPermission()
  ) {}

//  var fabOnClick: () -> Unit = {
//    when (PackageManager.PERMISSION_GRANTED) {
//      ContextCompat.checkSelfPermission(
//        context,
//        Manifest.permission.RECORD_AUDIO
//      ) -> {
//        keyboardController?.hide()
//        transcribingStateFlow.value = TranscribingState.TRANSCRIBING
//      }
//
//      else -> {
//        launcher.launch(Manifest.permission.RECORD_AUDIO)
//      }
//    }
//  }

//  var fabIcon = @Composable { Icon(Icons.Filled.Mic, "speech input") }

//  if (transcribingState.value == TranscribingState.TRANSCRIBING) {
//    fabOnClick = {
//      transcribingStateFlow.value = TranscribingState.NOT_TRANSCRIBING
//    }
//    fabIcon = @Composable { Icon(Icons.Filled.MicOff, "stop speech input") }
//  }

  BottomAppBar(
    modifier = Modifier
      .imePadding(),
    contentPadding = PaddingValues(20.dp),
  ) {
    Spacer(Modifier.weight(1f))

//    FloatingActionButton(
//      modifier = Modifier.padding(end = 20.dp),
//      onClick = fabOnClick
//    ) {
//      fabIcon()
//    }

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