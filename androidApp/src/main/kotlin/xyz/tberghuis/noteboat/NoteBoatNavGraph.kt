package xyz.tberghuis.noteboat

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import xyz.tberghuis.noteboat.screen.EditNoteScreen
import xyz.tberghuis.noteboat.screen.HomeScreen
import androidx.navigation.navDeepLink
import xyz.tberghuis.noteboat.screen.NewNoteScreen
import xyz.tberghuis.noteboat.screen.SettingsScreen
import xyz.tberghuis.noteboat.screen.TrashScreen

@Composable
fun NoteBoatNavGraph() {
  val navController = rememberNavController()
  CompositionLocalProvider(LocalNavController provides navController) {
    NavHost(navController = navController, startDestination = "home") {
      composable("home") { HomeScreen() }
      composable("new-note") {
        NewNoteScreen()
      }
      composable(
        "new-note/{navParam}",
        arguments = listOf(
          navArgument("navParam") { type = NavType.StringType },
        ),
        deepLinks = listOf(navDeepLink { uriPattern = "noteboat://noteboat/new-note/{navParam}" })
      ) {
//      val navParam: String? = backStackEntry.arguments?.getString("navParam")
        NewNoteScreen()
      }
      composable(
        "edit-note/{noteId}", arguments = listOf(
          navArgument("noteId") { type = NavType.IntType },
        )
      ) {
//      val noteId: Int = backStackEntry.arguments?.getInt("noteId")!!
        // noteid should be in the viewmodel savehandlestate
        EditNoteScreen()
      }
      composable("settings") {
        SettingsScreen()
      }
      composable("trash") {
        TrashScreen()
      }
    }
  }
}