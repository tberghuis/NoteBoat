package xyz.tberghuis.noteboat.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import xyz.tberghuis.noteboat.screen.EditNoteScreen
import xyz.tberghuis.noteboat.screen.HomeScreen
import xyz.tberghuis.noteboat.screen.NewNoteScreen
import xyz.tberghuis.noteboat.screen.SettingsScreen
import xyz.tberghuis.noteboat.screen.TrashScreen
import xyz.tberghuis.noteboat.vm.NewNoteViewModel

@Composable
fun NoteBoatNavDisplay(
  initialBackStack: SnapshotStateList<NavKey> = mutableStateListOf(RouteHome)
) {
  val configuration = SavedStateConfiguration {
    serializersModule = navSerializationModule
  }
  val backStack = rememberNavBackStack(configuration, *initialBackStack.toTypedArray())
  CompositionLocalProvider(LocalBackStackState provides backStack) {

    NavDisplay(
      entryDecorators = listOf(
        rememberSaveableStateHolderNavEntryDecorator(),
        rememberViewModelStoreNavEntryDecorator()
      ),
      backStack = backStack,
      onBack = { backStack.removeLastOrNull() },
      entryProvider = entryProvider {
        entry<RouteHome> {
          HomeScreen()
        }
        entry<RouteNewNote> { key ->
          val vm: NewNoteViewModel = koinViewModel(
            parameters = {
              parametersOf(key.voice)
            }
          )
          NewNoteScreen(vm)
        }
        entry<RouteEditNote> { key ->
          EditNoteScreen(key.noteId)
        }
        entry<RouteSettings> { key ->
          SettingsScreen()
        }
        entry<RouteTrash> { key ->
          TrashScreen()
        }
      }
    )
  }
}