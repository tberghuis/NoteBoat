package xyz.tberghuis.noteboat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import xyz.tberghuis.noteboat.data.NoteDao
import xyz.tberghuis.noteboat.data.OptionDao
import xyz.tberghuis.noteboat.ui.theme.NoteBoatTheme
import xyz.tberghuis.noteboat.data.appDatabase
import xyz.tberghuis.noteboat.tmp.tmp02.TmpScreen

class MainActivity : ComponentActivity() {
  private lateinit var noteDao: NoteDao
  private lateinit var optionDao: OptionDao

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    noteDao = appDatabase.noteDao()
    optionDao = appDatabase.optionDao()

    // only if started by lock screen notification
    if (intent != null && intent.hasExtra(LOCK_SCREEN_EXTRA_START) && intent.getBooleanExtra(
        LOCK_SCREEN_EXTRA_START,
        false
      )
    ) {
      setShowWhenLocked(true)
    }
    enableEdgeToEdge()
    setContent {
      NoteBoatTheme {
//        Surface(modifier = Modifier.fillMaxSize()) {
//          NoteBoatNavGraph()
//        }
        TmpScreen()
      }
    }
  }

  override fun onStop() {
    setShowWhenLocked(false)
    super.onStop()
  }
}