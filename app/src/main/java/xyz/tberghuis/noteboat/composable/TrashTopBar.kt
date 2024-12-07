package xyz.tberghuis.noteboat.composable

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import xyz.tberghuis.noteboat.LocalNavController
import xyz.tberghuis.noteboat.R

@Composable
fun TrashTopBar() {
  val navController = LocalNavController.current
  TopAppBar(
    modifier = Modifier
      .statusBarsPadding(),
    title = { Text("Trash") },
    navigationIcon = {
      IconButton(onClick = { navController.navigateUp() }) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = stringResource(R.string.back)
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
