package xyz.tberghuis.noteboat.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun SettingsScreen() {

  Scaffold() { padding ->
    Column(
      modifier = Modifier
        .padding(padding)
        .fillMaxSize()
        .background(Color.Green)
    ) {
      Text(
        "settings screen",
            modifier= Modifier.fillMaxSize(),
        color = Color.Black,
      )

    }
  }

}


