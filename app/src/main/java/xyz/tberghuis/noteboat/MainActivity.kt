package xyz.tberghuis.noteboat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import xyz.tberghuis.noteboat.data.NoteDao
import xyz.tberghuis.noteboat.data.OptionDao
import xyz.tberghuis.noteboat.screen.EditNoteScreen
import xyz.tberghuis.noteboat.screen.HomeScreen
import xyz.tberghuis.noteboat.ui.theme.NoteBoatTheme
import androidx.navigation.navDeepLink
import xyz.tberghuis.noteboat.data.appDatabase
import xyz.tberghuis.noteboat.screen.NewNoteScreen
import xyz.tberghuis.noteboat.screen.SettingsScreen
import xyz.tberghuis.noteboat.screen.TrashScreen

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

//    CoroutineScope(Dispatchers.IO).launch {
//      migrateLegacy(application, optionDao, noteDao)
//    }

    WindowCompat.setDecorFitsSystemWindows(window, false)
    setContent {
      NoteBoatTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
          NoteBoatNavGraph()
        }
      }
    }
  }

  override fun onStop() {
    setShowWhenLocked(false)
    super.onStop()
  }
}

