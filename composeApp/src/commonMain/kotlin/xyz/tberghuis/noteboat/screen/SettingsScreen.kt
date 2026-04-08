package xyz.tberghuis.noteboat.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import noteboat.composeapp.generated.resources.Res
import noteboat.composeapp.generated.resources.back
import noteboat.composeapp.generated.resources.settings
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import xyz.tberghuis.noteboat.nav.LocalBackStackState
import xyz.tberghuis.noteboat.nav.navigateUp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
  val backStack = LocalBackStackState.current
  Scaffold(
    topBar = {
      TopAppBar(
        modifier = Modifier,
        title = { Text(stringResource(Res.string.settings)) },
        navigationIcon = {
          IconButton(onClick = { backStack.navigateUp() }) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = stringResource(Res.string.back)
            )
          }
        },
      )
    },
  ) { padding ->
    SettingsContent(padding)
  }
}


@Composable
expect fun SettingsContent(padding: PaddingValues)


@Composable
fun SettingsScreenRow(
  horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween,
  content: @Composable RowScope.() -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(15.dp),
    horizontalArrangement = horizontalArrangement,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    content()
  }
}