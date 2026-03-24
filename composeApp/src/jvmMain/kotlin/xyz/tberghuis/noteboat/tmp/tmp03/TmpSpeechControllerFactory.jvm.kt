package xyz.tberghuis.noteboat.tmp.tmp03

import androidx.compose.runtime.MutableState
import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import xyz.tberghuis.noteboat.controller.RecognitionListenerEvent
import xyz.tberghuis.noteboat.controller.SpeechController
import xyz.tberghuis.noteboat.controller.TranscribingState

actual class TmpSpeechControllerFactory {
  actual fun create(

    transcribingStateFlow: MutableStateFlow<TranscribingState>,
    textFieldValueState: MutableState<TextFieldValue>,
    updateDb: (String) -> Unit
  ): SpeechController {
    return object : SpeechController {
      override var setMusicMute: Boolean
        get() = TODO("Not yet implemented")
        set(value) {}
      override var setNotificationMute: Boolean
        get() = TODO("Not yet implemented")
        set(value) {}
      override val recognitionListenerEventSharedFlow: MutableSharedFlow<RecognitionListenerEvent>
        get() = TODO("Not yet implemented")
      override val partialResultsFlow: MutableSharedFlow<String>
        get() = TODO("Not yet implemented")
      override val resultsFlow: MutableSharedFlow<String>
        get() = TODO("Not yet implemented")
      override var baseTextFieldValue: TextFieldValue
        get() = TODO("Not yet implemented")
        set(value) {}

      override suspend fun run() {
        TODO("Not yet implemented")
      }

    }
  }

}