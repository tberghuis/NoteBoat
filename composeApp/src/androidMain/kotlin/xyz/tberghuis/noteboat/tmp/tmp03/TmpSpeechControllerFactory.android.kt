package xyz.tberghuis.noteboat.tmp.tmp03

import android.content.Context

actual class TmpSpeechControllerFactory
  (private val context: Context) {
  actual fun create(): String {
    return "factory android context: $context"
  }
}