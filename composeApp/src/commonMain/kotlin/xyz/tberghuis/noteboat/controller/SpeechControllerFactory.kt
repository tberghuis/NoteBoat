package xyz.tberghuis.noteboat.controller

import androidx.compose.runtime.MutableState
import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.coroutines.flow.MutableStateFlow

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class SpeechControllerFactory {
  fun create(
    transcribingStateFlow: MutableStateFlow<TranscribingState>,
    textFieldValueState: MutableState<TextFieldValue>,
    updateDb: (String) -> Unit
  ): SpeechController
}