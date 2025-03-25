package xyz.tberghuis.noteboat.tmp.tmp03

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.InteractionSource
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
import kotlinx.coroutines.flow.drop

@Preview
@Composable
fun PushToTalk() {

  val interactionSource = remember { MutableInteractionSource() }
  interactionSource.onIsPressedStateChanged(
    onPressBegin = {
      logd("press begin")
    },
    onPressEnd = {
      logd("press end")
    },
  )

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

    Button(
      onClick = {},
      modifier = Modifier.pointerInput(Unit) {
        detectTapGestures(
          onPress = {
            logd("press start")
            tryAwaitRelease()
            logd("press finish")
          }
        )
      }
    ) {
      Text("detectTapGestures does not work")
    }

    Text(
      "willitblend works",
      modifier = Modifier.pointerInput(Unit) {
        detectTapGestures(
          onPress = {
            logd("press start")
            tryAwaitRelease()
            logd("press finish")
          }
        )
      }
    )
  }
}

@SuppressLint("ComposableNaming")
@Composable
fun InteractionSource.onIsPressedStateChanged(
  onPressBegin: () -> Unit,
  onPressEnd: () -> Unit,
  // dropFirst = true prevents onPressEnd running on first composition
  dropFirst: Boolean = true
) {
  val isPressed by this.collectIsPressedAsState()
  LaunchedEffect(this) {
    snapshotFlow { isPressed }
      .drop(
        if (dropFirst) 1 else 0
      ).collect {
        when (it) {
          true -> {
            onPressBegin()
          }

          false -> {
            onPressEnd()
          }
        }
      }
  }
}