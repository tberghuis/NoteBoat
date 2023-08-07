package xyz.tberghuis.noteboat.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
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
    modifier = Modifier.systemBarsPadding(), // why is this not needed for other routes???

    topBar = {
      TopAppBar(
        title = { Text("from R.strings") },
        navigationIcon = { },
      )
    },
  ) { padding ->
    Column(
      modifier = Modifier
        .padding(padding)
        .fillMaxSize()
//        .background(Color.Green)
    ) {

      Text(
        "settings screen",
      )

    }
  }

}


