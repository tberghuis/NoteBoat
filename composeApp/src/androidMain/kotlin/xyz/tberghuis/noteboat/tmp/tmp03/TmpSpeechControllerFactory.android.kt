package xyz.tberghuis.noteboat.tmp.tmp03

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.coroutines.flow.MutableStateFlow
import xyz.tberghuis.noteboat.controller.SpeechController
import xyz.tberghuis.noteboat.controller.SpeechControllerAndroid
import xyz.tberghuis.noteboat.controller.TranscribingState

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class SpeechControllerFactory
  (private val context: Context) {
  actual fun create(

    transcribingStateFlow: MutableStateFlow<TranscribingState>,
    textFieldValueState: MutableState<TextFieldValue>,
    updateDb: (String) -> Unit
  ): SpeechController {
    return SpeechControllerAndroid(
      context, transcribingStateFlow, textFieldValueState, updateDb
    )
  }
}