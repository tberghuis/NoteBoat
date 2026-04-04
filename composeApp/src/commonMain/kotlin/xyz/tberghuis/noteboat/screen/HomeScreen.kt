package xyz.tberghuis.noteboat.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import noteboat.composeapp.generated.resources.Res
import noteboat.composeapp.generated.resources.app_name
import noteboat.composeapp.generated.resources.settings
import noteboat.composeapp.generated.resources.trash
import org.jetbrains.compose.resources.stringResource
import xyz.tberghuis.noteboat.composable.HomeContent
import xyz.tberghuis.noteboat.nav.LocalBackStackState
import xyz.tberghuis.noteboat.nav.RouteNewNote
import xyz.tberghuis.noteboat.nav.RouteSettings
import xyz.tberghuis.noteboat.nav.RouteTrash

@Composable
fun HomeScreen() {
  val backStack = LocalBackStackState.current
  Scaffold(
    topBar = { HomeTopBar() },
    floatingActionButtonPosition = FabPosition.End,
    floatingActionButton = {
      FloatingActionButton(
        onClick = {
          backStack.add(RouteNewNote(false))
        }) {
        Icon(Icons.Filled.Add, "new note")
      }
    },
    content = { contentPadding ->
      Box(
        Modifier
          .padding(contentPadding)
      ) {
        HomeContent()
      }
    },
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar() {
  val backStack = LocalBackStackState.current
  TopAppBar(
    modifier = Modifier,
    title = { Text(stringResource(Res.string.app_name)) },
    actions = {
      IconButton(onClick = {
        backStack.add(RouteTrash)
      }) {
        Icon(Icons.Filled.Delete, stringResource(Res.string.trash))
      }
      IconButton(onClick = {
        backStack.add(RouteSettings)
      }) {
        Icon(Icons.Filled.Settings, stringResource(Res.string.settings))
      }
    }
  )
}