package xyz.tberghuis.noteboat.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import xyz.tberghuis.noteboat.vm.TranscribingState

@Composable
fun OnPauseLifecycleEvent(
  transcribingStateFlow: MutableStateFlow<TranscribingState>
) {
  val scope = rememberCoroutineScope()
  OnLifecycleEvent { owner, event ->
    // do stuff on event
    when (event) {
      Lifecycle.Event.ON_PAUSE -> {
        scope.launch {
          transcribingStateFlow.emit(TranscribingState.NOT_TRANSCRIBING)
        }
      }
      else -> { /* other stuff */
      }
    }
  }
}

@Composable
fun OnLifecycleEvent(onEvent: (owner: LifecycleOwner, event: Lifecycle.Event) -> Unit) {
  val eventHandler = rememberUpdatedState(onEvent)
  val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

  DisposableEffect(lifecycleOwner.value) {
    val lifecycle = lifecycleOwner.value.lifecycle
    val observer = LifecycleEventObserver { owner, event ->
      eventHandler.value(owner, event)
    }

    lifecycle.addObserver(observer)
    onDispose {
      lifecycle.removeObserver(observer)
    }
  }
}