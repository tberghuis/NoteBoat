package xyz.tberghuis.noteboat.composable

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomAppBar
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.MutableStateFlow
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

  var fabOnClick: () -> Unit = {
    when (PackageManager.PERMISSION_GRANTED) {
      ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.RECORD_AUDIO
      ) -> {
        keyboardController?.hide()
        transcribingStateFlow.value = TranscribingState.TRANSCRIBING
      }

      else -> {
        launcher.launch(Manifest.permission.RECORD_AUDIO)
      }
    }
  }

  var fabIcon = @Composable { Icon(Icons.Filled.Mic, "speech input") }

  if (transcribingState.value == TranscribingState.TRANSCRIBING) {
    fabOnClick = {
      transcribingStateFlow.value = TranscribingState.NOT_TRANSCRIBING
    }
    fabIcon = @Composable { Icon(Icons.Filled.MicOff, "stop speech input") }
  }

  BottomAppBar(
    modifier = Modifier
      .navigationBarsPadding()
      .imePadding(),
    backgroundColor = MaterialTheme.colors.background,
    contentPadding = PaddingValues(20.dp),
  ) {
    Spacer(Modifier.weight(1f))
    FloatingActionButton(
      modifier = Modifier.padding(end = 20.dp),
      onClick = fabOnClick
    ) {
      fabIcon()
    }
    FloatingActionButton(
      onClick = onComplete
    ) {
      Icon(Icons.Filled.Save, contentDescription = "save")
    }
  }
}