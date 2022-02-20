package xyz.tberghuis.noteboat.controller

import android.content.Context
import android.content.Context.AUDIO_SERVICE
import android.content.Intent
import android.media.AudioManager
import android.media.AudioManager.ADJUST_MUTE
import android.media.AudioManager.ADJUST_UNMUTE
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import xyz.tberghuis.noteboat.vm.NewNoteViewModel
import xyz.tberghuis.noteboat.vm.TranscribingState
import java.util.*
import kotlinx.coroutines.flow.collect



// TODO, VM creates this with appContext and passes in callbacks
// receiveResults receivePartialResults
// also pass in transcribingStateFlow
class SpeechController(
  context: Context,
  val vm: NewNoteViewModel,
  val scope: CoroutineScope
) {

  private val speechRecognizerIntent: Intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
  private val speechRecognizer: SpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)

  val audioManager = context.getSystemService(AUDIO_SERVICE) as AudioManager
  var isMuted = false

  val recognitionListenerEventSharedFlow: MutableSharedFlow<RecognitionListenerEvent> =
    MutableSharedFlow()

  val partialResultsFlow = MutableSharedFlow<String>()
  val resultsFlow = MutableSharedFlow<String>()

  var baseTextFieldValue = TextFieldValue()

  init {
    speechRecognizerIntent.putExtra(
      RecognizerIntent.EXTRA_LANGUAGE_MODEL,
      RecognizerIntent.LANGUAGE_MODEL_FREE_FORM,
    )
    speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
    speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
    setRecognitionListener(speechRecognizer, vm, this, scope)

    scope.launch {
      vm.transcribingStateFlow.collect {
        when (it) {
          TranscribingState.TRANSCRIBING -> {
            Log.d("xxx", "transcribing")

            baseTextFieldValue = vm.noteTextFieldValue

            startListening()
          }
          TranscribingState.NOT_TRANSCRIBING -> {
            Log.d("xxx", "not transcribing")
            stopListening()
          }
        }
      }
    }
    scope.launch {
      recognitionListenerEventSharedFlow.debounce {
        if (it == RecognitionListenerEvent.ON_ERROR) {
          100L
        } else {
          0L
        }
      }.collect {
        when (it) {
          RecognitionListenerEvent.ON_ERROR, RecognitionListenerEvent.ON_RESULTS -> {
            continueListening()
          }
        }
      }
    }
    collectResults()
  }

  // do it basic to start
  private fun collectResults() {

    fun appendAtCursor(result: String): TextFieldValue {
      if (result.isNotEmpty()) {
        val selectionStart = baseTextFieldValue.selection.start
        val text = "${
          baseTextFieldValue.text.substring(
            0,
            selectionStart
          )
        } $result ${baseTextFieldValue.text.substring(selectionStart)}"
        val newCursorPos = selectionStart + result.length + 2
        val textRange = TextRange(newCursorPos, newCursorPos)
//        vm.textFieldValue = vm.textFieldValue.copy(text, textRange)
        return TextFieldValue(text, textRange)
      }
      return baseTextFieldValue
    }

    scope.launch {
      partialResultsFlow.collect {
        // call ITextFieldViewModel.receiveSpeechPartialResult
        vm.noteTextFieldValue = appendAtCursor(it)
      }
    }
    scope.launch {
      resultsFlow.collect {
        // the order of assignment is pedantic
        baseTextFieldValue = appendAtCursor(it)
        // call ITextFieldViewModel.receiveSpeechResult
        vm.updateNewNoteDraft(baseTextFieldValue.text)
        vm.noteTextFieldValue = baseTextFieldValue
      }
    }
  }

  private fun startListening() {
    speechRecognizer.startListening(speechRecognizerIntent)
  }

  private fun stopListening() {
    audioManager.adjustStreamVolume(
      AudioManager.STREAM_NOTIFICATION,
      ADJUST_UNMUTE,
      0
    )
    isMuted = false
    speechRecognizer.stopListening()
  }

  private fun continueListening() {
    if (vm.transcribingStateFlow.value == TranscribingState.TRANSCRIBING) {
      startListening()
    }
  }

  fun emitRecognitionListenerEvent(e: RecognitionListenerEvent) {
    scope.launch {
      recognitionListenerEventSharedFlow.emit(e)
    }
  }

}


fun setRecognitionListener(
  speechRecognizer: SpeechRecognizer,
  vm: NewNoteViewModel,
  speechController: SpeechController,
  scope: CoroutineScope
) {

  speechRecognizer.setRecognitionListener(object : RecognitionListener {
    override fun onReadyForSpeech(p0: Bundle?) {
      Log.d("xxx", "onReadyForSpeech")
    }

    override fun onBeginningOfSpeech() {
      Log.d("xxx", "onBeginningOfSpeech")

      // i could do something fancy with combining flows, meh
      if (!speechController.isMuted) {
        speechController.audioManager.adjustStreamVolume(
          AudioManager.STREAM_NOTIFICATION,
          ADJUST_MUTE,
          0
        )
        speechController.isMuted = true
      }
    }

    override fun onRmsChanged(p0: Float) {
//        Log.d("xxx", "onRmsChanged")
    }

    override fun onBufferReceived(p0: ByteArray?) {
      Log.d("xxx", "onBufferReceived")
    }

    override fun onEndOfSpeech() {
      Log.d("xxx", "onEndOfSpeech ${System.currentTimeMillis()}")
    }

    override fun onError(p0: Int) {
      Log.d("xxx", "onError $p0 ${System.currentTimeMillis()}")
      speechController.emitRecognitionListenerEvent(RecognitionListenerEvent.ON_ERROR)
    }

    override fun onResults(p0: Bundle?) {
      Log.d("xxx", "onResults ${System.currentTimeMillis()}")
      p0?.let {
        val data = p0.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        Log.d("xxx", data.toString())

        data?.get(0)?.let {
//          speechManagerFresh.results = it
          scope.launch {
            speechController.resultsFlow.emit(it)
          }
        }
      }
      speechController.emitRecognitionListenerEvent(RecognitionListenerEvent.ON_RESULTS)
    }

    override fun onPartialResults(p0: Bundle?) {
      Log.d("xxx", "onPartialResults")
      p0?.let {
        val data = p0.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        Log.d("xxx", data.toString())
        data?.get(0)?.let {
          scope.launch {
            speechController.partialResultsFlow.emit(it)
          }
        }
      }
//      speechManagerFresh.emitRecognitionListenerEvent(RecognitionListenerEvent.ON_PARTIAL_RESULTS)

    }

    override fun onEvent(p0: Int, p1: Bundle?) {
      Log.d("xxx", "onEvent")
    }
  })
}


enum class RecognitionListenerEvent {
  ON_READY_FOR_SPEECH,
  ON_ERROR,
  ON_RESULTS,
  ON_END_OF_SPEECH,
  ON_PARTIAL_RESULTS
}