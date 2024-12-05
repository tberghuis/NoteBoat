package xyz.tberghuis.noteboat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
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
import xyz.tberghuis.noteboat.screen.NewNoteScreen
import xyz.tberghuis.noteboat.screen.SettingsScreen
import xyz.tberghuis.noteboat.tmp2.TrashScreen

class MainActivity : ComponentActivity() {
  private lateinit var noteDao: NoteDao
  private lateinit var optionDao: OptionDao

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    noteDao = (application as MainApplication).appDatabase.noteDao()
    optionDao = (application as MainApplication).appDatabase.optionDao()

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
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
          MainApp()
        }
      }
    }
  }

  override fun onStop() {
    setShowWhenLocked(false)
    super.onStop()
  }
}

@Composable
fun MainApp() {
  val navController = rememberNavController()

  // todo startDestination home
  NavHost(navController = navController, startDestination = "trash") {
    composable("home") { HomeScreen(navController = navController) }
    // todo add nav argument newNote=true
    // easier to duplicate ui
    composable("new-note") {
      NewNoteScreen(navController = navController)
    }
    composable(
      "new-note/{navParam}", arguments = listOf(
        navArgument("navParam") { type = NavType.StringType },
      ), deepLinks = listOf(navDeepLink { uriPattern = "noteboat://noteboat/new-note/{navParam}" })
    ) {
//      val navParam: String? = backStackEntry.arguments?.getString("navParam")
      NewNoteScreen(navController = navController)
    }
    composable(
      "edit-note/{noteId}", arguments = listOf(
        navArgument("noteId") { type = NavType.IntType },
      )
    ) {
//      val noteId: Int = backStackEntry.arguments?.getInt("noteId")!!
      // noteid should be in the viewmodel savehandlestate
      EditNoteScreen(navController = navController)
    }
    composable("settings") {
      SettingsScreen(navController = navController)
    }


    composable("trash") {
      TrashScreen()
    }


  }
}