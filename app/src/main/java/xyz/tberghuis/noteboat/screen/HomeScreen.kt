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
import androidx.compose.ui.res.stringResource
import xyz.tberghuis.noteboat.LocalNavController
import xyz.tberghuis.noteboat.R
import xyz.tberghuis.noteboat.composable.HomeContent

@Composable
fun HomeScreen() {
  val navController = LocalNavController.current
  Scaffold(
    topBar = { HomeTopBar() },
    floatingActionButtonPosition = FabPosition.End,
    floatingActionButton = {
      FloatingActionButton(
        onClick = {
          navController.navigate("new-note")
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
  val navController = LocalNavController.current
  TopAppBar(
    modifier = Modifier,
    title = { Text(stringResource(R.string.app_name)) },
    actions = {
      IconButton(onClick = {
        navController.navigate("trash")
      }) {
        Icon(Icons.Filled.Delete, stringResource(R.string.trash))
      }
      IconButton(onClick = {
        navController.navigate("settings")
      }) {
        Icon(Icons.Filled.Settings, stringResource(R.string.settings))
      }
    }
  )
}