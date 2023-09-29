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
import androidx.room.Room
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import xyz.tberghuis.noteboat.data.LegacyDatabase
import xyz.tberghuis.noteboat.data.Note
import xyz.tberghuis.noteboat.data.NoteDao
import xyz.tberghuis.noteboat.data.OptionDao
import xyz.tberghuis.noteboat.screen.EditNoteScreen
import xyz.tberghuis.noteboat.screen.HomeScreen
import xyz.tberghuis.noteboat.ui.theme.NoteBoatTheme
import java.io.File
import javax.inject.Inject
import androidx.navigation.navDeepLink
import xyz.tberghuis.noteboat.screen.SettingsScreen
import xyz.tberghuis.noteboat.tmp2.NewNoteScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  @Inject
  lateinit var noteDao: NoteDao

  @Inject
  lateinit var optionDao: OptionDao

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


    CoroutineScope(Dispatchers.IO).launch {
      migrateLegacy()
    }

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

  private suspend fun migrateLegacy() {
    val runMigration = optionDao.getOption("run_legacy_migration")
    if (runMigration != "true") {
      // logd("runMigration $runMigration")
      return
    }
    // eagerly update??? probably bad to assume migration will complete, DOITWRONG
    optionDao.updateOption("run_legacy_migration", "false")

    val file = File(applicationContext.filesDir, "../app_flutter/notes.db")
    if (!file.exists()) {
      // logd("no legacy database found")
      return
    }

    val legacyDb = Room.databaseBuilder(
      applicationContext, LegacyDatabase::class.java, file.path
    ).build()
    // logd("legacyDb $legacyDb")

    val legacyNotes = legacyDb.legacyNoteDao().getAll()
    // logd("legacyNotes $legacyNotes")

    legacyNotes.forEach {
      val noteText = it.noteText!!
      fun toEpoch(s: String?): Long {
        return s!!.toLocalDateTime().toInstant(TimeZone.currentSystemDefault())
          .toEpochMilliseconds()
      }

      val createdEpoch = toEpoch(it.createdDate)
      val modifiedEpoch = toEpoch(it.modifiedDate)
      val note =
        Note(noteText = noteText, createdEpoch = createdEpoch, modifiedEpoch = modifiedEpoch)
      noteDao.insertAll(note)
    }
  }
}

@Composable
fun MainApp() {
  val navController = rememberNavController()

  NavHost(navController = navController, startDestination = "home") {
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


  }
}