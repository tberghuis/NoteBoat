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
import android.speech.SpeechRecognizer.*
import androidx.compose.runtime.MutableState
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import xyz.tberghuis.noteboat.utils.logd
import java.util.*

class SpeechControllerAndroid(
  context: Context,
  private val transcribingStateFlow: MutableStateFlow<TranscribingState>,
  private val textFieldValueState: MutableState<TextFieldValue>,
  private val updateDb: (String) -> Unit
) : SpeechController {

  private val speechRecognizerIntent: Intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
  private val speechRecognizer: SpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)

  private val audioManager = context.getSystemService(AUDIO_SERVICE) as AudioManager

  //  var setMute = false
  override var setMusicMute = false
  override var setNotificationMute = false

  override val recognitionListenerEventSharedFlow: MutableSharedFlow<RecognitionListenerEvent> =
    MutableSharedFlow()

  override val partialResultsFlow = MutableSharedFlow<String>()
  override val resultsFlow = MutableSharedFlow<String>()

  override var baseTextFieldValue = TextFieldValue()

  init {
    speechRecognizerIntent.putExtra(
      RecognizerIntent.EXTRA_LANGUAGE_MODEL,
      RecognizerIntent.LANGUAGE_MODEL_FREE_FORM,
    )
    speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
    speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)

//    speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "site.thomasberghuis.noteboat")
//    speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "xyz.tberghuis.noteboat")

  }

  override suspend fun run() {
    coroutineScope {
      setRecognitionListener(
        speechRecognizer,
        this,
      )
      launch {
        transcribingStateFlow.collect {
          when (it) {
            TranscribingState.TRANSCRIBING -> {
              logd("transcribing")

              baseTextFieldValue = textFieldValueState.value

              startListening()
            }

            TranscribingState.NOT_TRANSCRIBING -> {
              logd("not transcribing")
              stopListening()
            }
          }
        }
      }
      launch {
        recognitionListenerEventSharedFlow.collect {
          when (it) {
            RecognitionListenerEvent.ON_RESULTS, RecognitionListenerEvent.ON_ERROR_CONTINUE
              -> {
              continueListening()
            }

            else -> {
              // todo
            }
//            RecognitionListenerEvent.ON_ERROR_CONTINUE,
//            -> {
////              speechRecognizer.cancel()
//              speechRecognizer.stopListening()
//              delay(200)
//              continueListening()
//            }
//            RecognitionListenerEvent.ON_ERROR_OTHER -> {
//              transcribingStateFlow.emit(TranscribingState.NOT_TRANSCRIBING)
//            }
          }
        }
      }
      collectResults()
    }
  }

  // do it basic to start
  private suspend fun collectResults() {
    coroutineScope {
      launch {
        partialResultsFlow.collect {
          // call ITextFieldViewModel.receiveSpeechPartialResult
          // run callback
          textFieldValueState.value = appendAtCursor(baseTextFieldValue, it)
        }
      }
      launch {
        resultsFlow.collect {
          baseTextFieldValue = appendAtCursor(baseTextFieldValue, it)
          updateDb(baseTextFieldValue.text)
          textFieldValueState.value = baseTextFieldValue
        }
      }
    }
  }

  private fun startListening() {
    speechRecognizer.startListening(speechRecognizerIntent)
  }

  private fun stopListening() {
    fun unmute(setMute: Boolean, stream: Int) {
      try {
        if (setMute) {
          audioManager.adjustStreamVolume(
            stream,
            ADJUST_UNMUTE,
            0
          )
        }
      } catch (e: SecurityException) {
        // will get SecurityException when in Do Not Disturb state
        // println(e)
      }
    }

    unmute(setNotificationMute, AudioManager.STREAM_NOTIFICATION)
    unmute(setMusicMute, AudioManager.STREAM_MUSIC)
    setNotificationMute = false
    setMusicMute = false

    speechRecognizer.stopListening()
  }

  private fun continueListening() {
    logd("continueListening")
    if (transcribingStateFlow.value == TranscribingState.TRANSCRIBING) {
      startListening()
    }
  }

  private fun setRecognitionListener(
    speechRecognizer: SpeechRecognizer,
    scope: CoroutineScope,
  ) {

    fun emitRecognitionListenerEvent(e: RecognitionListenerEvent) {
      scope.launch {
        recognitionListenerEventSharedFlow.emit(e)
      }
    }

    speechRecognizer.setRecognitionListener(object : RecognitionListener {
      override fun onReadyForSpeech(p0: Bundle?) {
        logd("onReadyForSpeech")
      }

      override fun onBeginningOfSpeech() {
        logd("onBeginningOfSpeech")
        // don't worry about zen mode cause i catch exception
//      val zenMode: Int = try {
//        Settings.Global.getInt(context.contentResolver, "zen_mode")
//      } catch (e: Settings.SettingNotFoundException) {
//        0
//      }

        fun muteStream(stream: Int): Boolean {
          val isMuted = audioManager.isStreamMute(stream)
          if (isMuted) {
            return false
          }
          try {
            audioManager.adjustStreamVolume(
              AudioManager.STREAM_NOTIFICATION,
              ADJUST_MUTE,
              0
            )
          } catch (e: SecurityException) {
            // Do not Disturb
          }
          return true
        }
        if (!setNotificationMute) {
          setNotificationMute = muteStream(AudioManager.STREAM_NOTIFICATION)
        }
        if (!setMusicMute) {
          setMusicMute = muteStream(AudioManager.STREAM_MUSIC)
        }
      }

      override fun onRmsChanged(p0: Float) {
//        logd("onRmsChanged")
      }

      override fun onBufferReceived(p0: ByteArray?) {
        logd("onBufferReceived")
      }

      override fun onEndOfSpeech() {
        logd("onEndOfSpeech ${System.currentTimeMillis()}")
      }

      override fun onError(p0: Int) {
        logd("onError $p0 ${System.currentTimeMillis()}")

        when (p0) {
          ERROR_NO_MATCH, ERROR_SPEECH_TIMEOUT -> {
            emitRecognitionListenerEvent(RecognitionListenerEvent.ON_ERROR_CONTINUE)
          }

          else -> {
            // TODO snackbar show error
            emitRecognitionListenerEvent(RecognitionListenerEvent.ON_ERROR_OTHER)
          }
          // TODO all other errors, emit not_transcribing to transcribingflow
        }
      }

      override fun onResults(p0: Bundle?) {
        logd("onResults ${System.currentTimeMillis()}")
        p0?.let {
          val data = p0.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
          logd(data.toString())

          data?.get(0)?.let {
            scope.launch {
              resultsFlow.emit(it)
            }
          }
        }
        emitRecognitionListenerEvent(RecognitionListenerEvent.ON_RESULTS)
      }

      override fun onPartialResults(p0: Bundle?) {
        logd("onPartialResults")
        p0?.let {
          val data = p0.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
          logd(data.toString())
          data?.get(0)?.let {
            scope.launch {
              partialResultsFlow.emit(it)
            }
          }
        }
      }

      override fun onEvent(p0: Int, p1: Bundle?) {
        logd("onEvent")
      }
    })
  }


}
