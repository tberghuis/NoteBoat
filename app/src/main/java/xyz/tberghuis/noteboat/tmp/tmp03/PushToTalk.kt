package xyz.tberghuis.noteboat.tmp.tmp03

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import xyz.tberghuis.noteboat.utils.logd
import androidx.compose.runtime.getValue

@Preview
@Composable
fun PushToTalk() {

  val interactionSource = remember { MutableInteractionSource() }
  val isPressed by interactionSource.collectIsPressedAsState()

  LaunchedEffect(interactionSource) {
    snapshotFlow { isPressed }.collect {
      when (it) {
        true -> {
          logd("press start")
        }

        false -> {
          logd("press stop")
        }
      }
    }
  }

  Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text("push to talk")
    Button(
      onClick = {},
      interactionSource = interactionSource,
    ) {
      Text("button")
    }

    Text(
      "willitblend",
      modifier = Modifier.pointerInput(Unit) {
        detectTapGestures(
          onPress = {
            logd("press start")
            tryAwaitRelease()
            logd("press finish")
          }
        )
      },
    )
  }
}