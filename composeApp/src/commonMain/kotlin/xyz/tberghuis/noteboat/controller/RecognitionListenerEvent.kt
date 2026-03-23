package xyz.tberghuis.noteboat.controller

enum class RecognitionListenerEvent {
  ON_READY_FOR_SPEECH,

  ON_ERROR_CLIENT,
  ON_ERROR_CONTINUE,
  ON_ERROR_OTHER,

  ON_RESULTS,
  ON_END_OF_SPEECH,
  ON_PARTIAL_RESULTS
}