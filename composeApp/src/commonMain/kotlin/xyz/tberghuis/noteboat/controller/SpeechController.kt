package xyz.tberghuis.noteboat.controller


//import androidx.compose.runtime.MutableState
//import androidx.compose.ui.text.input.TextFieldValue
//import kotlinx.coroutines.coroutineScope
//import kotlinx.coroutines.launch
//import xyz.tberghuis.noteboat.utils.logd
//import xyz.tberghuis.noteboat.controller.TranscribingState
//
//class XxxSpeechController(
//  private val context: Context,
//  private val transcribingStateFlow: MutableStateFlow<TranscribingState>,
//  private val textFieldValueState: MutableState<TextFieldValue>,
//  private val updateDb: (String) -> Unit
//) {
//
//  private val speechRecognizerIntent: Intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
//  private val speechRecognizer: SpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
//
//  val audioManager = context.getSystemService(AUDIO_SERVICE) as AudioManager
//
//  //  var setMute = false
//  var setMusicMute = false
//  var setNotificationMute = false
//
//  val recognitionListenerEventSharedFlow: MutableSharedFlow<RecognitionListenerEvent> =
//    MutableSharedFlow()
//
//  val partialResultsFlow = MutableSharedFlow<String>()
//  val resultsFlow = MutableSharedFlow<String>()
//
//  var baseTextFieldValue = TextFieldValue()
//
//  init {
//    speechRecognizerIntent.putExtra(
//      RecognizerIntent.EXTRA_LANGUAGE_MODEL,
//      RecognizerIntent.LANGUAGE_MODEL_FREE_FORM,
//    )
//    speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
//    speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
//
////    speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "site.thomasberghuis.noteboat")
////    speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "xyz.tberghuis.noteboat")
//
//  }
//
//  suspend fun run() {
//    coroutineScope {
//      setRecognitionListener(speechRecognizer, this@XxxSpeechController, this, audioManager, context)
//      launch {
//        transcribingStateFlow.collect {
//          when (it) {
//            TranscribingState.TRANSCRIBING -> {
//              logd("transcribing")
//
//              baseTextFieldValue = textFieldValueState.value
//
//              startListening()
//            }
//            TranscribingState.NOT_TRANSCRIBING -> {
//              logd("not transcribing")
//              stopListening()
//            }
//          }
//        }
//      }
//      launch {
//        recognitionListenerEventSharedFlow.collect {
//          when (it) {
//            RecognitionListenerEvent.ON_RESULTS, RecognitionListenerEvent.ON_ERROR_CONTINUE
//            -> {
//              continueListening()
//            }
//            else -> {
//              // todo
//            }
////            RecognitionListenerEvent.ON_ERROR_CONTINUE,
////            -> {
//////              speechRecognizer.cancel()
////              speechRecognizer.stopListening()
////              delay(200)
////              continueListening()
////            }
////            RecognitionListenerEvent.ON_ERROR_OTHER -> {
////              transcribingStateFlow.emit(TranscribingState.NOT_TRANSCRIBING)
////            }
//          }
//        }
//      }
//      collectResults()
//    }
//  }
//
//  // do it basic to start
//  private suspend fun collectResults() {
//    coroutineScope {
//      launch {
//        partialResultsFlow.collect {
//          // call ITextFieldViewModel.receiveSpeechPartialResult
//          // run callback
//          textFieldValueState.value = appendAtCursor(baseTextFieldValue, it)
//        }
//      }
//      launch {
//        resultsFlow.collect {
//          baseTextFieldValue = appendAtCursor(baseTextFieldValue, it)
//          updateDb(baseTextFieldValue.text)
//          textFieldValueState.value = baseTextFieldValue
//        }
//      }
//    }
//  }
//
//  private fun startListening() {
//    speechRecognizer.startListening(speechRecognizerIntent)
//  }
//
//  private fun stopListening() {
//    fun unmute(setMute: Boolean, stream: Int) {
//      try {
//        if (setMute) {
//          audioManager.adjustStreamVolume(
//            stream,
//            ADJUST_UNMUTE,
//            0
//          )
//        }
//      } catch (e: SecurityException) {
//        // will get SecurityException when in Do Not Disturb state
//        // println(e)
//      }
//    }
//
//    unmute(setNotificationMute, AudioManager.STREAM_NOTIFICATION)
//    unmute(setMusicMute, AudioManager.STREAM_MUSIC)
//    setNotificationMute = false
//    setMusicMute = false
//
//    speechRecognizer.stopListening()
//  }
//
//  private fun continueListening() {
//    logd("continueListening")
//    if (transcribingStateFlow.value == TranscribingState.TRANSCRIBING) {
//      startListening()
//    }
//  }
//}
