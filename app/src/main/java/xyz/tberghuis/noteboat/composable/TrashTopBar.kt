package xyz.tberghuis.noteboat.composable

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import xyz.tberghuis.noteboat.LocalNavController
import xyz.tberghuis.noteboat.R

@OptIn(ExperimentalMaterial3Api::class)
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
