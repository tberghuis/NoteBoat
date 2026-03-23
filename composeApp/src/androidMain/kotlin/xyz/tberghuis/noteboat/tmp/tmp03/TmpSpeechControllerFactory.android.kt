package xyz.tberghuis.noteboat.tmp.tmp03

import android.content.Context

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class TmpSpeechControllerFactory
  (private val context: Context) {
  actual fun create(): String {
    return "factory android context: $context"
  }
}