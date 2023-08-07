package xyz.tberghuis.noteboat.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun SettingsScreen() {

  Scaffold(
    topBar = {
      TopAppBar(
        modifier = Modifier
          .statusBarsPadding(),
        title = { Text("from R.strings") },
        navigationIcon = { },
      )
    },
  ) { padding ->
    Column(
      modifier = Modifier
        .padding(padding)
        .fillMaxSize()
    ) {

      Text(
        "settings screen",
      )

    }
  }

}


