package xyz.tberghuis.noteboat.composable

import android.annotation.SuppressLint
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.getValue
import kotlinx.coroutines.flow.drop

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