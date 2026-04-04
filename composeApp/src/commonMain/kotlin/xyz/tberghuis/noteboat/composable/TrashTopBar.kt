package xyz.tberghuis.noteboat.composable

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import noteboat.composeapp.generated.resources.Res
import noteboat.composeapp.generated.resources.back
import org.jetbrains.compose.resources.stringResource
import xyz.tberghuis.noteboat.nav.LocalBackStackState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrashTopBar() {
  val backStack = LocalBackStackState.current
  TopAppBar(
    modifier = Modifier,
    title = { Text("Trash") },
    navigationIcon = {
      IconButton(onClick = { backStack.removeLastOrNull() }) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = stringResource(Res.string.back)
        )
      }
    },
    actions = {
      // todo empty trash
      // todo restore all
//      IconButton(onClick = {
//        navController.navigate("settings")
//      }) {
//        Icon(Icons.Filled.Settings, stringResource(R.string.settings))
//      }
    }
  )
}