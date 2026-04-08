package xyz.tberghuis.noteboat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import xyz.tberghuis.noteboat.nav.NoteBoatNavDisplay
import xyz.tberghuis.noteboat.nav.RouteHome
import xyz.tberghuis.noteboat.nav.RouteNewNote
import xyz.tberghuis.noteboat.ui.theme.NoteBoatTheme

class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // only if started by lock screen notification
    if (intent != null && intent.hasExtra(LOCK_SCREEN_EXTRA_START) && intent.getBooleanExtra(
        LOCK_SCREEN_EXTRA_START,
        false
      )
    ) {
      setShowWhenLocked(true)
    }
    enableEdgeToEdge()

    val initialBackStack = mutableStateListOf<NavKey>(RouteHome)
    println("MainActivity intent data ${intent.data}")
    if (intent.data.toString() == "noteboat://noteboat/new-note/new_voice_note") {
      initialBackStack.add(RouteNewNote(true))
    }

    setContent {
      // todo replace with App()
      NoteBoatTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
          NoteBoatNavDisplay(initialBackStack)
        }
      }
    }
  }

  override fun onStop() {
    setShowWhenLocked(false)
    super.onStop()
  }
}