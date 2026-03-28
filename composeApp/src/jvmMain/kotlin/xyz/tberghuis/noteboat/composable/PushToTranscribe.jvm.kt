package xyz.tberghuis.noteboat.composable

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.MutableStateFlow
import xyz.tberghuis.noteboat.controller.TranscribingState

@Composable
actual fun PushToTranscribe(transcribingStateFlow: MutableStateFlow<TranscribingState>) {
}