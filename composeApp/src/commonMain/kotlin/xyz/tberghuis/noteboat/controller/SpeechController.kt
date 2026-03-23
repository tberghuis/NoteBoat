package xyz.tberghuis.noteboat.controller

import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.coroutines.flow.MutableSharedFlow

interface SpeechController {
  var setMusicMute: Boolean
  var setNotificationMute: Boolean
  val recognitionListenerEventSharedFlow: MutableSharedFlow<RecognitionListenerEvent>
  val partialResultsFlow: MutableSharedFlow<String>
  val resultsFlow: MutableSharedFlow<String>
  var baseTextFieldValue: TextFieldValue
  suspend fun run()
}
