package xyz.tberghuis.noteboat

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import com.google.accompanist.insets.ProvideWindowInsets
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
import xyz.tberghuis.noteboat.screen.NewNoteScreen
import xyz.tberghuis.noteboat.ui.theme.NoteBoatTheme
import xyz.tberghuis.noteboat.utils.logd
import xyz.tberghuis.noteboat.vm.NewNoteViewModel
import xyz.tberghuis.noteboat.vm.TranscribingState
import java.io.File
import javax.inject.Inject
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  @Inject
  lateinit var noteDao: NoteDao

  @Inject
  lateinit var optionDao: OptionDao

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // doitwrong
//    val feature = intent.extras?.getString("feature")
//    logd("feature $feature")

    CoroutineScope(Dispatchers.IO).launch {
      migrateLegacy()
    }

    WindowCompat.setDecorFitsSystemWindows(window, false)

    setContent {
      NoteBoatTheme {
        ProvideWindowInsets {
          // A surface container using the 'background' color from the theme
          Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            MainApp()
          }
        }
      }
    }
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
      applicationContext,
      LegacyDatabase::class.java,
      file.path
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
  val intent = (LocalContext.current as Activity).intent
//  intent.extras?.getString("feature")

  NavHost(navController = navController, startDestination = "home") {
    composable("home") { HomeScreen(navController = navController) }
    // todo add nav argument newNote=true
    // easier to duplicate ui
    composable("new-note") {
      NewNoteScreen(navController = navController)
    }
    composable(
      "new-note/{navParam}",
      arguments = listOf(
        navArgument("navParam") { type = NavType.StringType },
      )
    ) { backStackEntry ->
//      val navParam: String? = backStackEntry.arguments?.getString("navParam")
      NewNoteScreen(navController = navController)
    }
    composable(
      "edit-note/{noteId}",
      arguments = listOf(
        navArgument("noteId") { type = NavType.IntType },
      )
    ) { backStackEntry ->
//      val noteId: Int = backStackEntry.arguments?.getInt("noteId")!!
      // noteid should be in the viewmodel savehandlestate
      EditNoteScreen(navController = navController)
    }
  }

// doing it wrong
  RunOnceEffect {
    when (intent.extras?.getString("feature")) {
      "new_voice_note" -> {
        logd("mainapp new voice note")
        navController.navigate("new-note/voice")
      }
    }
  }
}

// do it wrong
// i can't fully wrap my head around this but it is working for now
@Composable
fun RunOnceEffect(callback: suspend () -> Unit) {
  // https://stackoverflow.com/questions/69629427/prevent-launchedeffect-from-re-running-on-configuration-change
  var hasRun by rememberSaveable { mutableStateOf(false) }
  if (!hasRun) {
    LaunchedEffect(true) {
      callback()
      hasRun = true
    }
  }
}