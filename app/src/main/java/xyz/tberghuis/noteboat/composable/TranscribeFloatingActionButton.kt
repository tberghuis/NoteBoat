package xyz.tberghuis.noteboat.composable

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.core.content.ContextCompat
import com.google.accompanist.insets.navigationBarsWithImePadding
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import xyz.tberghuis.noteboat.utils.logd
import xyz.tberghuis.noteboat.vm.TranscribingState

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TranscribeFloatingActionButton(
  transcribingStateFlow: MutableStateFlow<TranscribingState>
) {
//  val viewModel: NewNoteViewModel = hiltViewModel()
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

  FloatingActionButton(
    modifier = Modifier.navigationBarsWithImePadding(),
    onClick = fabOnClick
  ) {
    fabIcon()
  }


}